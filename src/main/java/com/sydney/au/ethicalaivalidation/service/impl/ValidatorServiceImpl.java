package com.sydney.au.ethicalaivalidation.service.impl;

import com.sydney.au.ethicalaivalidation.domain.*;
import com.sydney.au.ethicalaivalidation.repository.*;
import com.sydney.au.ethicalaivalidation.service.ValidatorService;
import com.sydney.au.ethicalaivalidation.utils.ServiceUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

@Transactional
@Service
public class ValidatorServiceImpl implements ValidatorService {

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

    public ValidatorServiceImpl(UsersRepository usersRepository, ProjectassignRepository projectassignRepository, ProjectsRepository projectsRepository, CompanyRepository companyRepository, EthicalconcernsRepository ethicalconcernsRepository, QuestionsRepository questionsRepository, SegmentsRepository segmentsRepository, QuestiontypeRepository questiontypeRepository, SubquestionsRepository subquestionsRepository, PrinciplesRepository principlesRepository, AnswerRepository answerRepository, ValidatorfeedbackRepository validatorfeedbackRepository, QuestionfeedbackRepository questionfeedbackRepository, ProjectvalidationRepository projectvalidationRepository, SegmentsummaryRepository segmentsummaryRepository, QuestionstatusRepository questionstatusRepository) {
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
    public List<Map<String, Object>> getProjectList(String userName) {
        ArrayList<Map<String, Object>> projectList = new ArrayList<>();
        projectsRepository.findByStatus(3).forEach(x -> {
            TreeMap<String, Object> project = new TreeMap<>();
            project.put("projectid", x.getId());
            project.put("projectname", x.getProjectname());
            project.put("date", x.getCreatedtime());
            projectList.add(project);
        });
        return projectList;
    }

    @Override
    public Map<String, Object> getProjectDetail(String projectName, String validatorName) {
        //查询信息
        Projects project = projectsRepository.findByProjectname(projectName);
        Users validator = usersRepository.findByUsername(validatorName);
        List<Ethicalconcerns> answerList = ethicalconcernsRepository.findByProjectid(project.getId());
        HashSet<Integer> subQueIdSet = new HashSet<>();
        HashSet<Integer> queIdSet = new HashSet<>();
        HashSet<Integer> segIdSet = new HashSet<>();
        HashSet<Integer> principleIdSet = new HashSet<>();
        answerList.forEach(x -> {
            subQueIdSet.add(x.getSubquesid());
            queIdSet.add(x.getQuestionid());

        });
        List<Subquestions> subQuestionList = new ArrayList<>();
        subquestionsRepository.findAllById(new ArrayList<>(subQueIdSet)).forEach(subQuestionList::add);
        List<Questions> questionList = new ArrayList<>();
        questionsRepository.findAllById(new ArrayList<>(queIdSet))
                .forEach(x -> {
                    questionList.add(x);
                    segIdSet.add(x.getSegmentid());
                });
        List<Segments> segmentList = segmentsRepository.findByIdIn(new ArrayList<>(segIdSet));
        segmentList.forEach(x -> {
            principleIdSet.add(x.getPrincipleid());
        });
        List<Principles> principleList = principlesRepository.findByIdIn(new ArrayList<>(principleIdSet));
        //构建结果
        TreeMap<String, Object> res = new TreeMap<>();
        res.put("projectname", project.getProjectname());
        res.put("description", project.getDescription());
        res.put("creator", usersRepository.findById(project.getCreatorid()).get().getUsername());
        res.put("createdtime", project.getCreatedtime());
        res.put("updatetime", validatorfeedbackRepository.findFirstByProjectidAndValidatoridOrderByCheckindexDesc(project.getId(), validator.getId()));
        //写principle的列表
        List<TreeMap<String, Object>> content = new ArrayList<>();
        //构建principle
        principleList.forEach(principle -> {
            TreeMap<String, Object> principleMap = new TreeMap<>();
            principleMap.put("principle", principle.getPrinciplename());
            ArrayList<TreeMap<String, Object>> principleContent = new ArrayList<>();
            //构建segment
            segmentList.forEach(segment -> {
                if (segment.getPrincipleid() == principle.getId()) {
                    TreeMap<String, Object> segmentMap = new TreeMap<>();
                    segmentMap.put("segmentid", segment.getId());
                    segmentMap.put("segment", segment.getSegmentname());
                    //此处需要进行null的判断
                    Optional<Segmentsummary> segmentSummary = segmentsummaryRepository.findByProjectidAndSegmentidAndValidatorid(
                            project.getId(),
                            segment.getId(),
                            validator.getId());
                    segmentMap.put("checksummary",
                            segmentSummary.isPresent() ? 1 : 0);
                    segmentMap.put("segmentcomment", segmentSummary.isPresent() ? segmentSummary.get().getSummary() : "");
                    ArrayList<TreeMap<String, Object>> segmentContent = new ArrayList<>();
                    //构建question
                    questionList.forEach(question -> {
                        if (question.getSegmentid() == segment.getId()) {
                            TreeMap<String, Object> questionMap = new TreeMap<>();
                            questionMap.put("questionid", question.getId());
                            questionMap.put("question", question.getQuestioncontent());
                            ArrayList<TreeMap<String, Object>> questionContent = new ArrayList<>();
                            subQuestionList.forEach(subquestion -> {
                                // 构建subquestion
                                if (subquestion.getQuestionid() == question.getId()) {
                                    TreeMap<String, Object> subQuestionMap = new TreeMap<>();
                                    subQuestionMap.put("subquesid", subquestion.getId());
                                    subQuestionMap.put("subquestion", subquestion.getId());
                                    Ethicalconcerns ethicalconcern = ethicalconcernsRepository.findByProjectidAndSubquesid(
                                            project.getId(), subquestion.getId());
                                    //判断ethicalconcern的记录不为空
                                    if (ethicalconcern != null) {
                                        subQuestionMap.put("answer",
                                                ethicalconcern.getAnswer());
                                        subQuestionMap.put("check", ethicalconcern.getFinished());
                                        List<Questionfeedback> feedback = questionfeedbackRepository.findByProjectidAndValidatoridAndSubquesid(
                                                project.getId(), validator.getId(), subquestion.getId());
                                        ArrayList<TreeMap<String, Object>> comments = new ArrayList<>();
                                        //判断feedback的记录不为空
                                        if (feedback != null) {
                                            //构建comment
                                            for (Questionfeedback questionfeedback : feedback) {
                                                TreeMap<String, Object> comment = new TreeMap<>();
                                                comment.put("commenter", usersRepository.findById(questionfeedback.getValidatorid()).get().getUsername());
                                                comment.put("comment", questionfeedback.getContent());
                                                comment.put("commenttime", questionfeedback.getFeedbacktime());
                                                comments.add(comment);
                                            }
                                        }
                                        //插入comment
                                        subQuestionMap.put("quescomment", comments);
                                    }
                                    //放入空结果
                                    else {
                                        subQuestionMap.put("answer", "");
                                        subQuestionMap.put("check", 0);
                                        subQuestionMap.put("quescomment", new ArrayList<>());
                                    }
                                    questionContent.add(subQuestionMap);
                                }
                            });
                            //插入subquestion
                            questionMap.put("questioncontent", questionContent);
                            segmentContent.add(questionMap);
                        }
                    });
                    //插入question
                    segmentMap.put("segmentcontent", segmentContent);
                    principleContent.add(segmentMap);
                }
            });
            //插入segment
            principleMap.put("principlecontent", principleContent);
            content.add(principleMap);
        });
        //插入principle
        res.put("content", content);
        return res;
    }

    @Override
    public boolean addComment(String projectName, String validatorName, Integer subQuestionId, String comment, Boolean pass) {
        Projects project = projectsRepository.findByProjectname(projectName);
        Users validator = usersRepository.findByUsername(validatorName);
        Projectvalidation projectValidation = projectvalidationRepository.findByProjectidAndValidatorid(project.getId(), validator.getId());
        int lastCheckIndex = projectValidation.getChecknumber();
        if (questionfeedbackRepository.findByProjectidAndValidatoridAndSubquesid(project.getId(), validator.getId(), subQuestionId).isEmpty())
            questionfeedbackRepository.save(new Questionfeedback(project.getId(), validator.getId(), subQuestionId, lastCheckIndex + 1, comment, ServiceUtils.getNowTimeStamp()));
        else
            questionfeedbackRepository.updateByProjectIdAndValidatorIdAndSubquesid(project.getId(), validator.getId(), subQuestionId, lastCheckIndex + 1, comment, ServiceUtils.getNowTimeStamp());

        if (questionstatusRepository.findByProjectidAndValidatoridAndSubquesid(project.getId(), validator.getId(), subQuestionId).isPresent()) {
            questionstatusRepository.save(
                    new Questionstatus(project.getId(), validator.getId(), subQuestionId, pass ? 2 : 1));
        } else {
            questionstatusRepository.updateByProjectIdAndValidatorIdAndSubQuestionId(
                    project.getId(), validator.getId(), subQuestionId, 1);
        }
        return true;
    }

    @Override
    public boolean addSummary(String projectName, String validatorName, Integer segmentId, String comment) {
        Projects project = projectsRepository.findByProjectname(projectName);
        Users validator = usersRepository.findByUsername(validatorName);
        List<Questions> questions = questionsRepository.findBySegmentid(segmentId);
        List<Subquestions> subQuestions = subquestionsRepository.findByQuestionidIn(questions.parallelStream().map(Questions::getId).collect(Collectors.toList()));
        List<Questionstatus> subQuestionStatus = questionstatusRepository.findByProjectidAndSubquesidIn(project.getId(), subQuestions.parallelStream().map(Subquestions::getId).collect(Collectors.toList()));
        AtomicBoolean isAllPassed = new AtomicBoolean(true);
        subQuestionStatus.forEach(status -> {
            if (status.getStatus() == 1) isAllPassed.set(false);
        });
        if (!isAllPassed.get()) return false;
        segmentsummaryRepository.updateByProjectIdAndValidatorIdAndSubquesid(
                project.getId(),
                validator.getId(),
                segmentId,
                comment,
                ServiceUtils.getNowTimeStamp());
        return true;
    }

    @Override
    public boolean passProject(String projectName, String validatorName) {
        Projects project = projectsRepository.findByProjectname(projectName);
        Users validator = usersRepository.findByUsername(validatorName);
        List<Principles> principleList = principlesRepository.getPrincipleByProjectId(project.getId());
        List<Segments> segmentList = segmentsRepository.findByPrincipleidIn(principleList.stream().map(Principles::getId).collect(Collectors.toList()));
        List<Segmentsummary> summaryList = segmentsummaryRepository.findByProjectidAndValidatoridAndSegmentidIn(project.getId(), validator.getId(), segmentList.stream().map(Segments::getId).collect(Collectors.toList()));
        AtomicBoolean allFinish = new AtomicBoolean(true);
        segmentList.forEach(segments -> {
            boolean summaryIsOk = false;
            for (Segmentsummary summary : summaryList) {
                if (summary.getSegmentid() == segments.getId()) {
                    summaryIsOk = true;
                    break;
                }
            }
            if (!summaryIsOk)
                allFinish.set(false);
        });
        if (!allFinish.get()) return false;
        questionstatusRepository.updateByProjectId(project.getId(), 2);
        projectvalidationRepository.updateStatusByProjectId(project.getId(), 2);
        projectsRepository.updateStatusByProjectId(project.getId(), 5);
        return true;
    }

    @Override
    public boolean sendFeedback(String projectName, String validatorName) {
        Projects project = projectsRepository.findByProjectname(projectName);
        Users validator = usersRepository.findByUsername(validatorName);
        List<Questionfeedback> questionFeedback = questionfeedbackRepository.findByProjectidAndValidatorid(project.getId(), validator.getId());
        int createdindex = questionFeedback
                .stream().max((a, b) -> a.getCreatedindex() > b.getCreatedindex() ? 1 : -1).get()
                .getCreatedindex();
        int checknumber = projectvalidationRepository.findByProjectidAndValidatorid(project.getId(), validator.getId()).getChecknumber();
        if (createdindex != checknumber + 1)
            return false;
        projectvalidationRepository.addCheckNumberByProjectIdAndValidatorId(project.getId(), validator.getId());
        validatorfeedbackRepository.save(new Validatorfeedback(validator.getId(), project.getId(), ServiceUtils.getNowTimeStamp(), createdindex));
        List<Questionfeedback> latestFeedback = questionFeedback.parallelStream()
                .filter(feedback -> feedback.getCreatedindex() == createdindex).collect(Collectors.toList());
        ethicalconcernsRepository.updateFinishedByProjectIdAndSubquesidIn(project.getId(), latestFeedback.stream().map(Questionfeedback::getSubquesid).distinct().collect(Collectors.toList()), 1);
        projectsRepository.updateStatusByProjectId(project.getId(), 4);
        return true;
    }
}
