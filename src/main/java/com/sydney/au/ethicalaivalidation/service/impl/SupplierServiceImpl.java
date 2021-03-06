package com.sydney.au.ethicalaivalidation.service.impl;

import com.sydney.au.ethicalaivalidation.domain.*;
import com.sydney.au.ethicalaivalidation.entity.FeedbackMessage;
import com.sydney.au.ethicalaivalidation.repository.*;
import com.sydney.au.ethicalaivalidation.service.SupplierService;
import com.sydney.au.ethicalaivalidation.utils.ServiceUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

@Transactional
@Service
@Slf4j
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
    private final ProjectvalidationRepository projectvalidationRepository;
    private final SegmentsummaryRepository segmentsummaryRepository;
    private final QuestionstatusRepository questionstatusRepository;

    public SupplierServiceImpl(UsersRepository usersRepository, ProjectassignRepository projectassignRepository, ProjectsRepository projectsRepository, CompanyRepository companyRepository, EthicalconcernsRepository ethicalconcernsRepository, QuestionsRepository questionsRepository, SegmentsRepository segmentsRepository, QuestiontypeRepository questiontypeRepository, SubquestionsRepository subquestionsRepository, PrinciplesRepository principlesRepository, AnswerRepository answerRepository, ValidatorfeedbackRepository validatorfeedbackRepository, QuestionfeedbackRepository questionfeedbackRepository, ProjectvalidationRepository projectvalidationRepository, SegmentsummaryRepository segmentsummaryRepository, QuestionstatusRepository questionstatusRepository) {
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
        this.projectvalidationRepository = projectvalidationRepository;
        this.segmentsummaryRepository = segmentsummaryRepository;
        this.questionstatusRepository = questionstatusRepository;
    }

    @Override
    public List<Map<String, String>> listProject(String supplierName) {
        //?????????id
        Users supplier = usersRepository.findByUsername(supplierName);
        String companyName = companyRepository.findById(supplier.getCompanyid()).get().getCompanyname();
        int supplierId = supplier.getId();
        //????????????????????????id????????????assign time
        Map<Integer, List<Timestamp>> projectList = projectassignRepository.findBySupplierid(supplierId)
                .stream().collect(Collectors.toMap(Projectassign::getProjectid, x -> {
                    List<Timestamp> timestamps = new ArrayList<>();
                    timestamps.add(x.getAssigntime());
                    timestamps.add(x.getUpdatetime());
                    return timestamps;
                }));
        Set<Integer> projectIdList = projectList.keySet();
        //??????id?????????status???project
        List<Projects> allById = new ArrayList<>();
        projectsRepository.findAllById(projectIdList).forEach(x -> {
            if (x.getStatus() == 2
                    || x.getStatus() == 3
                    || x.getStatus() == 4
                    || x.getStatus() == 5)
                allById.add(x);
        });
        //????????????
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
        //??????supplier???project
        Users supplier = usersRepository.findByUsername(userName);
        Projects projects = projectsRepository.findByProjectname(projectName);
        //??????
        List<Projectassign> projectAssignList = projectassignRepository.findByProjectid(projects.getId());
        Map<String, String> res = new TreeMap<>();
        //FLAG???????????????????????????userid
        AtomicBoolean userContainFlag = new AtomicBoolean(false);
        //?????????????????????
        List<Integer> lockList = new ArrayList<>();
        projectAssignList.forEach(x -> {
            //???????????????user???assign
            if (x.getSupplierid() == supplier.getId()) {
                //???FLAG???????????????????????????userid
                userContainFlag.set(true);
                //???????????????user??????lock???????????????
                if (x.getLocked() == 2) {
                    projectassignRepository.unLockByProjectIdAndSupplierId(projects.getId(), supplier.getId());
                }
            }
            if (x.getLocked() == 2) {
                lockList.add(x.getLockedquestion());
            }
        });
        //??????????????????????????????userid
        if (!userContainFlag.get()) {
            return res;
        }
        //?????????ethicalconcerns
        List<Ethicalconcerns> ethicalList = ethicalconcernsRepository
                .findByProjectidAndFinished(projects.getId(), 1)
                .stream()
                .filter(x -> !lockList.contains(x.getQuestionid()))
                .collect(Collectors.toList());
        //?????????????????????
        int firstQuestionId = ethicalList.stream().mapToInt(Ethicalconcerns::getQuestionid).min().orElse(Integer.MAX_VALUE);
        int firstSubQuestion = ethicalList.stream().mapToInt(x -> {
            if (x.getQuestionid() == firstQuestionId) {
                return x.getSubquesid();
            }
            return Integer.MAX_VALUE;
        }).min().orElse(Integer.MAX_VALUE);
        //????????????
        res.put("questionid", String.valueOf(firstQuestionId));
        res.put("subquesid", String.valueOf(firstSubQuestion));
        return res;
    }

    @Override
    public Map<String, Object> getQuestionPage(String projectName, String userName, String questionId, String subquesId) {
        Map<String, Object> res = new TreeMap<>();
        //??????supplier???project
        Users supplier = usersRepository.findByUsername(userName);
        Projects project = projectsRepository.findByProjectname(projectName);
        //???projectassign??????
        projectassignRepository.lockByProjectIdAndSupplierId(
                project.getId(),
                supplier.getId(),
                Integer.parseInt(questionId),
                ServiceUtils.getNowTimeStamp());
        //??????????????????
        Questions question = questionsRepository.findById(Integer.parseInt(questionId)).get();
        Subquestions subquestion = subquestionsRepository.findById(Integer.parseInt(subquesId)).get();
        Segments segment = segmentsRepository.findById(question.getSegmentid()).get();
        String principleName = principlesRepository.findById(segment.getPrincipleid()).get().getPrinciplename();
        String segmentName = segment.getSegmentname();
        String questionContent = question.getQuestioncontent();
        String subQuesContent = subquestion.getContent();
        int questionType = questiontypeRepository.findById(subquestion.getQuestiontype()).get().getType();
        //????????????
        ArrayList<TreeMap<String, Object>> comments = new ArrayList<>();
        List<Questionfeedback> feedback = questionfeedbackRepository.findByProjectidAndSubquesid(
                project.getId(), subquestion.getId());
        //??????feedback??????????????????
        if (feedback != null) {
            //??????comment
            for (Questionfeedback questionfeedback : feedback) {
                TreeMap<String, Object> comment = new TreeMap<>();
                comment.put("commenter", usersRepository.findById(questionfeedback.getValidatorid()).get().getUsername());
                comment.put("comment", questionfeedback.getContent());
                comment.put("commenttime", questionfeedback.getFeedbacktime());
                comments.add(comment);
            }
        }
        //????????????
        res.put("projectid", project.getId());
        res.put("subquesid", subquesId);
        res.put("principle", principleName);
        res.put("segment", segmentName);
        res.put("questioncontent", questionContent);
        res.put("subquescontent", subQuesContent);
        res.put("questiontype", questionType);
        //??????comment
        res.put("quescomment", comments);
        return res;
    }


    @Override
    public Map<String, Object> postAnswer(String projectName, Integer questionId, Integer subQuestionId, Map<String, Object> answerMap) {
        Map<String, Object> res = new TreeMap<>();
        //???question type
        int questionTypeId = subquestionsRepository.findById(subQuestionId).get().getQuestiontype();
        Integer subQuestionType = questiontypeRepository.findById(questionTypeId).get().getType();
        int point = 0;
        String answer = "";
        //?????????
        if (subQuestionType.equals(1)) {
            int answerOption = (int) answerMap.get("option1");
            //??????????????????
            if (answerOption <= 0 && answerOption != -1) {
                return res;
            }
            //?????????
            else if (answerOption >= 1) {
                //????????????
                List<Answer> answerList = answerRepository.findBySubquesid(subQuestionId);
                //??????????????????
                if (answerList.stream().mapToInt(Answer::getAnswer).noneMatch(x -> x == answerOption)) return res;
                answer = String.valueOf(answerOption);
                //????????????
                for (Answer answer1 : answerList) {
                    if (answer1.getAnswer() == answerOption) point = answer1.getPoint();
                }
            }
            //?????????
            else {
                int linkOption = (int) answerMap.get("option2");
                if (linkOption == 1) {
                    answer = "link: " + answerMap.get("link");
                } else if (linkOption == 2) {
                    answer = "file: " + answerMap.get("submitedfile");
                }
            }
        }
        //???????????????
        else if (subQuestionType.equals(2)) {
            answer = (String) answerMap.get("text");
        } else {
            return res;
        }
        //????????????
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
        //??????????????????????????????
        Users supplier = usersRepository.findByUsername(userName);
        List<Projectassign> projectAssignList = projectassignRepository.findBySupplierid(supplier.getId());
        List<Integer> projectIdList = projectAssignList.stream().map(Projectassign::getProjectid).collect(Collectors.toList());
        //???????????????????????????
        List<Projects> projects = new ArrayList<>();
        projectsRepository.findAllById(projectIdList).forEach(projects::add);
        //???????????????validator feedback??????
        List<Validatorfeedback> validatorFeedback = validatorfeedbackRepository.findByProjectidIn(projectIdList);
        List<FeedbackMessage> feedbackMessages = new ArrayList<>();
        projects.forEach(project -> {
            //???project assign???????????????type=1
            if (project.getStatus() == 2 || project.getStatus() == 3) {
                Projectassign projectassign = projectAssignList.stream().filter(assign -> assign.getProjectid() == project.getId()).collect(Collectors.toList()).get(0);
                feedbackMessages.add(new FeedbackMessage(1,
                        project.getId(),
                        project.getProjectname(),
                        projectassign.getAssigntime(),
                        usersRepository.findById(project.getCreatorid()).get().getUsername()));
            }
            //???validator comment???????????????type=2
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
        //???list number
        for (int i = 0; i < feedbackMessages.size(); i++) {
            FeedbackMessage message = feedbackMessages.get(i);
            message.setListNumber(i);
            feedbackMessages.set(i, message);
        }
        return feedbackMessages.stream().map(FeedbackMessage::toResMap).collect(Collectors.toList());
    }

    @Override
    public List<Map<String, Object>> getValidatorFeedback(String projectName, String validatorName, String feedbackTime) {
        //??????project???validator???checkIndex???????????????
        Projects project = projectsRepository.findByProjectname(projectName);
        Users validator = usersRepository.findByUsername(validatorName);
        int checkIndex = validatorfeedbackRepository
                .findByProjectidAndValidatoridAndSendtime(project.getId(), validator.getId(), Timestamp.valueOf(LocalDateTime.parse(feedbackTime, DateTimeFormatter.ofPattern("yyyy-MM-ddHH:mm:ss")))).getCheckindex();
        //???????????????????????????????????????
        List<Questionfeedback> questionFeedbackList = questionfeedbackRepository.findByProjectidAndValidatoridAndCreatedindex(project.getId(), validator.getId(), checkIndex);
        List<Map<String, Object>> res = new ArrayList<>();
        questionFeedbackList.forEach(x -> {
            //????????????
            //todo ????????????map????????????
            Subquestions subquestion = subquestionsRepository.findById(x.getSubquesid()).get();
            Questions question = questionsRepository.findById(subquestion.getQuestionid()).get();
            Segments segment = segmentsRepository.findById(question.getSegmentid()).get();
            String principleName = principlesRepository.findById(segment.getPrincipleid()).get().getPrinciplename();
            Ethicalconcerns ethicalConcern = ethicalconcernsRepository.findByProjectidAndSubquesid(project.getId(), subquestion.getId());
            String segmentName = segment.getSegmentname();
            String questionContent = question.getQuestioncontent();
            String subQuesContent = subquestion.getContent();
            //??????map
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

    @Override
    public Map<String, Object> getReport(String projectName) {
        Projects project = projectsRepository.findByProjectname(projectName);
        //??????????????????
        //????????????Map
        List<Integer> supplierIds = projectassignRepository.findByProjectid(project.getId()).parallelStream().map(Projectassign::getSupplierid).distinct().collect(Collectors.toList());
        List<Integer> validatorIds = projectvalidationRepository.findByProjectid(project.getId()).parallelStream().map(Projectvalidation::getValidatorid).distinct().collect(Collectors.toList());
        Map<Integer, String> userNameMap = new HashMap<>();
        usersRepository.findAllById(new ArrayList<Integer>() {{
            addAll(supplierIds);
            addAll(validatorIds);
        }}).forEach(user -> userNameMap.put(user.getId(), user.getUsername()));
        //??????ethicalconcerns???list?????????Map
        List<Ethicalconcerns> ethicalconcernsList = ethicalconcernsRepository.findByProjectid(project.getId());
        Map<Integer, Ethicalconcerns> ethicalconcernsMap = ethicalconcernsList.parallelStream().collect(Collectors.toMap(Ethicalconcerns::getSubquesid, ethicalconcerns -> ethicalconcerns));
        //??????subquestions?????????Map
        Map<Integer, Subquestions> subquestionsMap = new HashMap<Integer, Subquestions>() {{
            subquestionsRepository.findAllById(ethicalconcernsMap.keySet()).forEach(x -> put(x.getId(), x));
        }};
        //??????summary?????????Map
        Map<Integer, Segmentsummary> segmentsummaryMap = new HashMap<Integer, Segmentsummary>() {{
            segmentsummaryRepository.findByProjectid(project.getId()).forEach(x -> put(x.getSegmentid(), x));
        }};
        //??????answer?????????Map
        Map<Integer, Answer> answerMap = new HashMap<Integer, Answer>() {{
            answerRepository.findAllById(ethicalconcernsList.parallelStream().map(Ethicalconcerns::getSubquesid).distinct().collect(Collectors.toList()))
                    .forEach(x -> {
                        if (x.getPoint() > get(x.getSubquesid()).getPoint())
                            put(x.getSubquesid(), x);
                    });
        }};
        //??????questions???list?????????Map
        List<Questions> questionsList = questionsRepository.findByIdIn(ethicalconcernsList.parallelStream().map(Ethicalconcerns::getQuestionid).collect(Collectors.toList()));
        Map<Integer, Questions> questionsMap = questionsList.parallelStream().collect(Collectors.toMap(Questions::getId, questions -> questions));
        //??????segments???list?????????Map
        List<Segments> segmentsList = segmentsRepository.findByIdIn(questionsList.parallelStream().map(Questions::getSegmentid).collect(Collectors.toList()));
        Map<Integer, Segments> segmentsMap = segmentsList.parallelStream().collect(Collectors.toMap(Segments::getId, segments -> segments));
        //??????principles???list
        List<Principles> principlesList = principlesRepository.findByIdIn(segmentsList.parallelStream().map(Segments::getPrincipleid).collect(Collectors.toList()));

        //??????????????????
        Map<String, Object> res = new TreeMap<>();
        res.put("projectname", project.getProjectname());
        res.put("description", project.getDescription());
        res.put("createdtime", project.getCreatedtime());
        res.put("creator", usersRepository.findById(project.getCreatorid()).get());

        List<Map<String, Object>> contentList = new ArrayList<>();
        principlesList.parallelStream().forEach(principle -> {
            Map<String, Object> summaryMap = new TreeMap<>();
            summaryMap.put("principle", principle.getPrinciplename());
            List<Map<String, Object>> principleContentList = new ArrayList<>();
            segmentsList.parallelStream().forEach(segment -> {
                if (segment.getPrincipleid() != principle.getId()) return;
                Map<String, Object> principleContentMap = new TreeMap<>();
                principleContentMap.put("segment", segment.getSegmentname());
                principleContentMap.put("summary", segmentsummaryMap.get(segment.getId()));
                List<Map<String, Object>> segmentContentList = new ArrayList<>();
                questionsList.parallelStream().forEach(question -> {
                    if (question.getSegmentid() != segment.getId()) return;
                    Map<String, Object> segmentContentMap = new TreeMap<>();
                    segmentContentMap.put("question", question.getQuestioncontent());
                    List<Map<String, Object>> questionContentList = new ArrayList<>();
                    ethicalconcernsList.parallelStream().forEach(ethicalconcern -> {
                        if (ethicalconcern.getQuestionid() != question.getId()) return;
                        Map<String, Object> questionContentMap = new TreeMap<>();
                        Subquestions subquestions = subquestionsMap.get(ethicalconcern.getSubquesid());
                        questionContentMap.put("subquestion", subquestions.getContent());
                        questionContentMap.put("level", subquestions.getLevel());
                        questionContentMap.put("youranswer", ethicalconcern.getAnswer());
                        questionContentMap.put("point", ethicalconcern.getPoints());
                        questionContentList.add(questionContentMap);
                    });
                    segmentContentMap.put("questioncontent", questionContentList);
                    segmentContentList.add(segmentContentMap);
                });
                principleContentMap.put("segmentcontent", segmentContentList);
                principleContentList.add(principleContentMap);
            });
            summaryMap.put("principlecontent", principleContentList);
            contentList.add(summaryMap);
        });
        res.put("content", contentList);
        return res;
    }

}
