package com.sydney.au.ethicalaivalidation.service.impl;

import com.sydney.au.ethicalaivalidation.domain.*;
import com.sydney.au.ethicalaivalidation.entity.FeedbackMessage;
import com.sydney.au.ethicalaivalidation.repository.*;
import com.sydney.au.ethicalaivalidation.service.SupplierService;
import com.sydney.au.ethicalaivalidation.utils.ServiceUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

@Transactional
@Service
public class SupplierServiceImpl implements SupplierService {

    private final UsersRepository usersRepository;
    private final ProjectassignRepository projectassignRepository;
    private final ProjectsRepository projectsRepository;
    private final CompanyRepository companyRepository;
    private final EthicalconcernsRepository ethicalconcernsRepository;
    private final QuestionsRepository questionsRepository;
    private final SegmentsRepository segmentsRepository;
    private final QuestiontypeRepository questiontypeRepository;
    private final SubquestionsRepository subquestionsRepository;
    private final PrinciplesRepository principlesRepository;
    private final AnswerRepository answerRepository;
    private final ValidatorfeedbackRepository validatorfeedbackRepository;
    private final QuestionfeedbackRepository questionfeedbackRepository;

    public SupplierServiceImpl(UsersRepository usersRepository, ProjectassignRepository projectassignRepository, ProjectsRepository projectsRepository, CompanyRepository companyRepository, EthicalconcernsRepository ethicalconcernsRepository, QuestionsRepository questionsRepository, SegmentsRepository segmentsRepository, QuestiontypeRepository questiontypeRepository, SubquestionsRepository subquestionsRepository, PrinciplesRepository principlesRepository, AnswerRepository answerRepository, ValidatorfeedbackRepository validatorfeedbackRepository, QuestionfeedbackRepository questionfeedbackRepository) {
        this.usersRepository = usersRepository;
        this.projectassignRepository = projectassignRepository;
        this.projectsRepository = projectsRepository;
        this.companyRepository = companyRepository;
        this.ethicalconcernsRepository = ethicalconcernsRepository;
        this.questionsRepository = questionsRepository;
        this.segmentsRepository = segmentsRepository;
        this.questiontypeRepository = questiontypeRepository;
        this.subquestionsRepository = subquestionsRepository;
        this.principlesRepository = principlesRepository;
        this.answerRepository = answerRepository;
        this.validatorfeedbackRepository = validatorfeedbackRepository;
        this.questionfeedbackRepository = questionfeedbackRepository;
    }

    @Override
    public List<Map<String, String>> listProject(String supplierName) {
        //找用户id
        Users supplier = usersRepository.findByUsername(supplierName);
        String companyName = companyRepository.findById(supplier.getCompanyid()).get().getCompanyname();
        int supplierId = supplier.getId();
        //找所有关联的项目id和对应的assign time
        Map<Integer, List<Timestamp>> projectList = projectassignRepository.findBySupplierid(supplierId)
                .stream().collect(Collectors.toMap(Projectassign::getProjectid, x -> {
                    List<Timestamp> timestamps = new ArrayList<>();
                    timestamps.add(x.getAssigntime());
                    timestamps.add(x.getUpdatetime());
                    return timestamps;
                }));
        Set<Integer> projectIdList = projectList.keySet();
        //根据id取符合status的project
        List<Projects> allById = new ArrayList<>();
        projectsRepository.findAllById(projectIdList).forEach(x -> {
            if (x.getStatus() == 2
                    || x.getStatus() == 3
                    || x.getStatus() == 4
                    || x.getStatus() == 5)
                allById.add(x);
        });
        //构建结果
        List<Map<String, String>> res = new ArrayList<>();
        allById.forEach(x -> {
            TreeMap<String, String> projectMap = new TreeMap<>();
            projectMap.put("projectid", String.valueOf(x.getId()));
            projectMap.put("projectname", x.getProjectname());
            projectMap.put("status", String.valueOf(x.getStatus()));
            projectMap.put("assigntime", String.valueOf(projectList.get(x.getId()).get(0)));
            projectMap.put("updatetime", String.valueOf(projectList.get(x.getId()).get(1)));
            projectMap.put("company", companyName);
            res.add(projectMap);
        });
        return res;
    }

    @Override
    public Map<String, String> getQuestion(String projectName, String userName) {
        //获取supplier和project
        Users supplier = usersRepository.findByUsername(userName);
        Projects projects = projectsRepository.findByProjectname(projectName);
        //获取
        List<Projectassign> projectAssignList = projectassignRepository.findByProjectid(projects.getId());
        Map<String, String> res = new TreeMap<>();
        //FLAG：该问题是否属于该userid
        AtomicBoolean userContainFlag = new AtomicBoolean(false);
        //锁住的问题列表
        List<Integer> lockList = new ArrayList<>();
        projectAssignList.forEach(x -> {
            //查找属于该user的assign
            if (x.getSupplierid() == supplier.getId()) {
                //写FLAG：该问题是否属于该userid
                userContainFlag.set(true);
                //如果此时该user处于lock状态，解锁
                if (x.getLocked() == 2) {
                    projectassignRepository.unLockByProjectIdAndSupplierId(projects.getId(), supplier.getId());
                }
            }
            if (x.getLocked() == 2) {
                lockList.add(x.getLockedquestion());
            }
        });
        //判断该问题是否属于该userid
        if (!userContainFlag.get()) {
            return res;
        }
        //筛选出ethicalconcerns
        List<Ethicalconcerns> ethicalList = ethicalconcernsRepository
                .findByProjectidAndFinished(projects.getId(), 1)
                .stream()
                .filter(x -> !lockList.contains(x.getQuestionid()))
                .collect(Collectors.toList());
        //取第一组第一个
        int firstQuestionId = ethicalList.stream().mapToInt(Ethicalconcerns::getQuestionid).min().orElse(Integer.MAX_VALUE);
        int firstSubQuestion = ethicalList.stream().mapToInt(x -> {
            if (x.getQuestionid() == firstQuestionId) {
                return x.getSubquesid();
            }
            return Integer.MAX_VALUE;
        }).min().orElse(Integer.MAX_VALUE);
        //构建结果
        res.put("questionid", String.valueOf(firstQuestionId));
        res.put("subquesid", String.valueOf(firstSubQuestion));
        return res;
    }

    @Override
    public Map<String, Object> getQuestionPage(String projectName, String userName, String questionId, String subquesId) {
        Map<String, Object> res = new TreeMap<>();
        //获取supplier和project
        Users supplier = usersRepository.findByUsername(userName);
        Projects projects = projectsRepository.findByProjectname(projectName);
        //去projectassign上锁
        projectassignRepository.lockByProjectIdAndSupplierId(
                projects.getId(),
                supplier.getId(),
                Integer.parseInt(questionId),
                ServiceUtils.getNowTimeStamp());
        //获取具体信息
        Questions question = questionsRepository.findById(Integer.parseInt(questionId)).get();
        Subquestions subquestion = subquestionsRepository.findById(Integer.parseInt(subquesId)).get();
        Segments segment = segmentsRepository.findById(question.getSegmentid()).get();
        String principleName = principlesRepository.findById(segment.getPrincipleid()).get().getPrinciplename();
        String segmentName = segment.getSegmentname();
        String questionContent = question.getQuestioncontent();
        String subQuesContent = subquestion.getContent();
        int questionType = questiontypeRepository.findById(subquestion.getQuestiontype()).get().getType();
        //构建结果
        res.put("projectid", projects.getId());
        res.put("subquesid", subquesId);
        res.put("principle", principleName);
        res.put("segment", segmentName);
        res.put("questioncontent", questionContent);
        res.put("subquescontent", subQuesContent);
        res.put("questiontype", questionType);
        return res;
    }


    @Override
    public Map<String, Object> postAnswer(String projectName, Integer questionId, Integer subQuestionId, Map<String, Object> answerMap) {
        Map<String, Object> res = new TreeMap<>();
        //查question type
        int questionTypeId = subquestionsRepository.findById(subQuestionId).get().getQuestiontype();
        Integer subQuestionType = questiontypeRepository.findById(questionTypeId).get().getType();
        int point = 0;
        String answer = "";
        //一般题
        if (subQuestionType.equals(1)) {
            int answerOption = (int) answerMap.get("option1");
            //如果选项为空
            if (answerOption <= 0 && answerOption != -1) {
                return res;
            }
            //选择题
            else if (answerOption >= 1) {
                //答案列表
                List<Answer> answerList = answerRepository.findBySubquesid(subQuestionId);
                //如果选项非法
                if (answerList.stream().mapToInt(Answer::getAnswer).noneMatch(x -> x == answerOption)) return res;
                answer = String.valueOf(answerOption);
                //查找分数
                for (Answer answer1 : answerList) {
                    if (answer1.getAnswer() == answerOption) point = answer1.getPoint();
                }
            }
            //链接题
            else {
                int linkOption = (int) answerMap.get("option2");
                if (linkOption == 1) {
                    answer = "link: " + answerMap.get("link");
                } else if (linkOption == 2) {
                    answer = "file: " + answerMap.get("submitedfile");
                }
            }
        }
        //调用接口题
        else if (subQuestionType.equals(2)) {
            answer = (String) answerMap.get("text");
        } else {
            return res;
        }
        //通用流程
        int projectId = projectsRepository.findByProjectname(projectName).getId();
        if (ethicalconcernsRepository.findByProjectidAndSubquesid(projectId, subQuestionId) == null) {
            Ethicalconcerns ethicalconcern = new Ethicalconcerns();
            ethicalconcern.setProjectid(projectId);
            ethicalconcern.setQuestionid(questionId);
            ethicalconcern.setSubquesid(subQuestionId);
            ethicalconcern.setAnswer(answer);
            ethicalconcern.setPoints(point);
            ethicalconcernsRepository.save(ethicalconcern);
        } else {
            ethicalconcernsRepository.updateAnswerAndPointsByProjectIdAndSubQuesId(projectId,subQuestionId,answer,point);
        }
        ethicalconcernsRepository.updateFinishedByProjectIdAndSubquesid(projectId,subQuestionId,2);
        res.put("projectid", projectId);
        res.put("projectname", projectName);
        return res;
    }

    @Override
    public List<Map<String, Object>> getAllMessage(String userName) {
        //获取用户与关联的项目
        Users supplier = usersRepository.findByUsername(userName);
        List<Projectassign> projectAssignList = projectassignRepository.findBySupplierid(supplier.getId());
        List<Integer> projectIdList = projectAssignList.stream().map(Projectassign::getProjectid).collect(Collectors.toList());
        //查找所有的项目信息
        List<Projects> projects = new ArrayList<>();
        projectsRepository.findAllById(projectIdList).forEach(projects::add);
        //查找所有的validator feedback信息
        List<Validatorfeedback> validatorFeedback = validatorfeedbackRepository.findByProjectidIn(projectIdList);
        List<FeedbackMessage> feedbackMessages = new ArrayList<>();
        projects.forEach(project -> {
            //将project assign放入结果，type=1
            if (project.getStatus() == 2 || project.getStatus() == 3) {
                Projectassign projectassign = projectAssignList.stream().filter(assign -> assign.getProjectid() == project.getId()).collect(Collectors.toList()).get(0);
                feedbackMessages.add(new FeedbackMessage(1,
                        project.getId(),
                        project.getProjectname(),
                        projectassign.getAssigntime(),
                        usersRepository.findById(project.getCreatorid()).get().getUsername()));
            }
            //将validator comment放入结果，type=2
            if (project.getStatus() > 3) {
                Projectassign projectassign = projectAssignList.stream().filter(assign -> assign.getProjectid() == project.getId()).collect(Collectors.toList()).get(0);
                feedbackMessages.add(new FeedbackMessage(1,
                        project.getId(),
                        project.getProjectname(),
                        projectassign.getAssigntime(),
                        usersRepository.findById(project.getCreatorid()).get().getUsername()));
                List<Validatorfeedback> feedbackList = validatorFeedback.stream().filter(feedback -> feedback.getProjectid() == project.getId()).collect(Collectors.toList());
                for (Validatorfeedback feedback : feedbackList) {
                    feedbackMessages.add(new FeedbackMessage(2,
                            project.getId(),
                            project.getProjectname(),
                            feedback.getSendtime(),
                            usersRepository.findById(feedback.getValidatorid()).get().getUsername()));
                }
            }
        });
        feedbackMessages.sort(Collections.reverseOrder());
        //写list number
        for (int i = 0; i < feedbackMessages.size(); i++) {
            FeedbackMessage message = feedbackMessages.get(i);
            message.setListNumber(i);
            feedbackMessages.set(i, message);
        }
        return feedbackMessages.stream().map(FeedbackMessage::toResMap).collect(Collectors.toList());
    }

    @Override
    public List<Map<String, Object>> getValidatorFeedback(String projectName, String validatorName, String feedbackTime) {
        //获取project，validator，checkIndex（版本号）
        Projects project = projectsRepository.findByProjectname(projectName);
        Users validator = usersRepository.findByUsername(validatorName);
        int checkIndex = validatorfeedbackRepository
                .findByProjectidAndValidatoridAndSendtime(project.getId(), validator.getId(), Timestamp.valueOf(feedbackTime)).getCheckindex();
        //获取该版本的所有子问题反馈
        List<Questionfeedback> questionFeedbackList = questionfeedbackRepository.findByProjectidAndValidatoridAndCreatedindex(project.getId(), validator.getId(), checkIndex);
        List<Map<String, Object>> res = new ArrayList<>();
        questionFeedbackList.forEach(x -> {
            //查询信息
            //todo 此处可用map缓存优化
            Subquestions subquestion = subquestionsRepository.findById(x.getSubquesid()).get();
            Questions question = questionsRepository.findById(x.getProjectid()).get();
            Segments segment = segmentsRepository.findById(question.getSegmentid()).get();
            String principleName = principlesRepository.findById(segment.getPrincipleid()).get().getPrinciplename();
            Ethicalconcerns ethicalConcern = ethicalconcernsRepository.findByProjectidAndSubquesid(project.getId(), subquestion.getId());
            String segmentName = segment.getSegmentname();
            String questionContent = question.getQuestioncontent();
            String subQuesContent = subquestion.getContent();
            //结果map
            Map<String, Object> feedbackMap = new TreeMap<>();
            feedbackMap.put("principle", principleName);
            feedbackMap.put("segment", segmentName);
            feedbackMap.put("questionid", question.getId());
            feedbackMap.put("questioncontent", questionContent);
            feedbackMap.put("subquesid", subquestion.getId());
            feedbackMap.put("subquescontent", subQuesContent);
            feedbackMap.put("youranswer", ethicalConcern.getAnswer());
            feedbackMap.put("comments", x.getContent());
            res.add(feedbackMap);
        });
        return res;
    }

    @Override
    public Map<String, Object> getAssignFeedback(String projectName) {
        Projects project = projectsRepository.findByProjectname(projectName);
        List<Principles> principles = principlesRepository.getPrincipleByProjectId(project.getId());
        Map<String, Object> res = new TreeMap<>();
        res.put("projectname", project.getProjectname());
        res.put("creatorname", usersRepository.findById(project.getCreatorid()).get().getUsername());
        res.put("feedbacktime", project.getCreatedtime());
        res.put("ethicalconcerns", principles.stream().map(x -> {
            Map<String, Object> map = new TreeMap<>();
            map.put("principleid", x.getId());
            map.put("principlename", x.getPrinciplename());
            return map;
        }).collect(Collectors.toList()));
        res.put("createdtime", project.getCreatedtime());
        res.put("status", project.getStatus());
        return res;
    }

}
