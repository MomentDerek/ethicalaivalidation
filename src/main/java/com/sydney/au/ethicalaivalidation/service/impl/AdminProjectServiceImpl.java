package com.sydney.au.ethicalaivalidation.service.impl;

import com.sydney.au.ethicalaivalidation.domain.*;
import com.sydney.au.ethicalaivalidation.repository.*;
import com.sydney.au.ethicalaivalidation.service.AdminProjectService;
import com.sydney.au.ethicalaivalidation.utils.ServiceUtils;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collector;
import java.util.stream.Collectors;

@Service
public class AdminProjectServiceImpl implements AdminProjectService {

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

    public AdminProjectServiceImpl(UsersRepository usersRepository, ProjectassignRepository projectassignRepository, ProjectsRepository projectsRepository, CompanyRepository companyRepository, EthicalconcernsRepository ethicalconcernsRepository, QuestionsRepository questionsRepository, SegmentsRepository segmentsRepository, QuestiontypeRepository questiontypeRepository, SubquestionsRepository subquestionsRepository, PrinciplesRepository principlesRepository, AnswerRepository answerRepository, ValidatorfeedbackRepository validatorfeedbackRepository, QuestionfeedbackRepository questionfeedbackRepository, ProjectvalidationRepository projectvalidationRepository, SegmentsummaryRepository segmentsummaryRepository, QuestionstatusRepository questionstatusRepository) {
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
    public List<Map<String, Object>> listAllProject() {
        List<Projects> projects = new ArrayList<>();
        projectsRepository.findAll().forEach(projects::add);
        return projects.parallelStream()
                .map(project -> {
                    TreeMap<String, Object> projectMap = new TreeMap<>();
                    projectMap.put("projectname", project.getProjectname());
                    projectMap.put("projectid", project.getId());
                    projectMap.put("createdtime", project.getCreatedtime());
                    projectMap.put("status", project.getStatus());
                    return projectMap;
                }).collect(Collectors.toList());
    }

    @Override
    public Map<String, Object> getProjectDetail(String projectName) {
        Projects project = projectsRepository.findByProjectname(projectName);
        int projectId = project.getId();
        //查询所有supplier/validator信息
        List<Projectassign> suppliersInfo = projectassignRepository.findByProjectid(projectId);
        List<Projectvalidation> validatorsInfo = projectvalidationRepository.findByProjectid(projectId);
        //创建用户缓存Map
        Map<Integer, Users> userInfos = new HashMap<>();
        usersRepository.findAllById(new ArrayList<Integer>() {{
            addAll(suppliersInfo.parallelStream().map(Projectassign::getSupplierid).collect(Collectors.toList()));
            addAll(validatorsInfo.parallelStream().map(Projectvalidation::getValidatorid).collect(Collectors.toList()));
        }}).forEach(user -> userInfos.put(user.getId(), user));
        //构建supplier信息
        List<Map<String, Object>> suppliers = suppliersInfo.parallelStream().map(supplier -> {
            Map<String, Object> supplierMap = new TreeMap<>();
            int id = supplier.getSupplierid();
            Users supplierInfo = userInfos.get(id);
            supplierMap.put("id", id);
            supplierMap.put("name", supplierInfo.getUsername());
            return supplierMap;
        }).collect(Collectors.toList());
        //构建validator信息
        List<Map<String, Object>> validators = validatorsInfo.parallelStream().map(validator -> {
            Map<String, Object> validatorMap = new TreeMap<>();
            int id = validator.getValidatorid();
            Users validatorInfo = userInfos.get(id);
            validatorMap.put("id", id);
            validatorMap.put("name", validatorInfo.getUsername());
            return validatorMap;
        }).collect(Collectors.toList());
        //构建ethicalConcerns信息
        List<Map<String, Object>> ethicalConcerns = ethicalconcernsRepository.findByProjectid(projectId)
                .parallelStream().map(ethicalconcern -> {
                    Map<String, Object> ethicalConcernsMap = new TreeMap<>();
                    ethicalConcernsMap.put("id", ethicalconcern.getId());
                    ethicalConcernsMap.put("subquestionid", ethicalconcern.getSubquesid());
                    ethicalConcernsMap.put("questionid", ethicalconcern.getQuestionid());
                    ethicalConcernsMap.put("answer", ethicalconcern.getAnswer());
                    ethicalConcernsMap.put("finished", ethicalconcern.getFinished());
                    return ethicalConcernsMap;
                }).collect(Collectors.toList());
        TreeMap<String, Object> res = new TreeMap<>();
        res.put("createdtime", project.getCreatedtime());
        res.put("creator", usersRepository.findById(project.getCreatorid()).get().getUsername());
        res.put("description", project.getDescription());
        res.put("status", project.getStatus());
        res.put("assignedsuppliers", suppliers);
        res.put("assignedvalidators", validators);
        res.put("ethicalconcerns", ethicalConcerns);
        return res;
    }

    @Override
    public List<Map<String, Object>> listOtherQuestion(String projectName) {
        Projects project = projectsRepository.findByProjectname(projectName);
        List<Ethicalconcerns> ethicalConcerns = ethicalconcernsRepository.findByProjectid(project.getId());
        //创建缓存容器
        List<Subquestions> leftSubQuestions = subquestionsRepository.findByIdNotIn(
                ethicalConcerns.parallelStream()
                        .map(Ethicalconcerns::getSubquesid).collect(Collectors.toList()));
        Map<Integer, Questions> leftQuestionsMap = questionsRepository.findByIdIn(
                leftSubQuestions.parallelStream()
                        .map(Subquestions::getQuestionid).distinct().collect(Collectors.toList()))
                .parallelStream().collect(Collectors.toMap(Questions::getId, questions -> questions));
        List<Integer> leftSegmentId = new ArrayList<>();
        leftQuestionsMap.forEach((key, question) -> leftSegmentId.add(question.getSegmentid()));
        Map<Integer, Segments> leftSegmentMap = segmentsRepository.findByIdIn(leftSegmentId)
                .parallelStream().collect(Collectors.toMap(Segments::getId, segments -> segments));
        List<Integer> leftPrincipleId = new ArrayList<>();
        leftSegmentMap.forEach((key, segment) -> leftPrincipleId.add(segment.getPrincipleid()));
        Map<Integer, Principles> leftPrinciples = principlesRepository.findByIdIn(leftPrincipleId)
                .stream().collect(Collectors.toMap(Principles::getId, principles -> principles));
        //构建结果
        return leftSubQuestions.parallelStream().map(subQuestion -> {
            TreeMap<String, Object> subQuestionMap = new TreeMap<>();
            subQuestionMap.put("subquesid", subQuestion.getId());
            subQuestionMap.put("subquestion", subQuestion.getContent());
            subQuestionMap.put("questiontype", subQuestion.getQuestiontype());
            Questions question = leftQuestionsMap.get(subQuestion.getQuestionid());
            subQuestionMap.put("questionid", question.getId());
            subQuestionMap.put("question", question.getQuestioncontent());
            Segments segment = leftSegmentMap.get(question.getSegmentid());
            subQuestionMap.put("segmentid", segment.getId());
            subQuestionMap.put("segment", segment.getSegmentname());
            Principles principle = leftPrinciples.get(segment.getPrincipleid());
            subQuestionMap.put("principleid", principle.getId());
            subQuestionMap.put("principle", principle.getPrinciplename());
            return subQuestionMap;
        }).collect(Collectors.toList());
    }

    @Override
    public boolean addOtherQuestion(String projectName, Integer projectId, Integer subQuesId) {
        Projects project = projectsRepository.findByProjectname(projectName);
        Optional<Subquestions> subQuesOptional = subquestionsRepository.findById(subQuesId);
        //判断subquestion id是否有效 && projectName是否有效 && 该subquestion是否已加入
        if (!subQuesOptional.isPresent()
                || project == null
                || project.getId() != projectId
                || ethicalconcernsRepository.findByProjectidAndSubquesid(projectId, subQuesId) != null)
            return false;
        Ethicalconcerns ethicalConcern = new Ethicalconcerns();
        ethicalConcern.setProjectid(projectId);
        ethicalConcern.setSubquesid(subQuesId);
        ethicalConcern.setQuestionid(subQuesOptional.get().getQuestionid());
        ethicalConcern.setFinished(1);
        ethicalconcernsRepository.save(ethicalConcern);
        return true;
    }

    @Override
    public boolean deleteQuestion(String projectName, Integer subQuesId) {
        Projects project = projectsRepository.findByProjectname(projectName);
        Optional<Subquestions> subQuesOptional = subquestionsRepository.findById(subQuesId);
        //判断subquestion id是否有效 && projectName是否有效 && 该subquestion是否已加入
        if (!subQuesOptional.isPresent()
                || project == null
                || ethicalconcernsRepository.findByProjectidAndSubquesid(project.getId(), subQuesId) == null)
            return false;
        ethicalconcernsRepository.deleteByProjectidAndSubquesid(project.getId(), subQuesId);
        return true;
    }

    @Override
    public List<Map<String, Object>> listAllValidator(String projectName) {
        return usersRepository.findByUsertype(3).parallelStream()
                .map(validator -> {
                    Map<String, Object> validatorMap = new TreeMap<>();
                    validatorMap.put("username", validator.getUsername());
                    validatorMap.put("userid", validator.getId());
                    validatorMap.put("company", validator.getCompanyid());
                    return validatorMap;
                }).collect(Collectors.toList());
    }

    @Override
    public boolean assignValidator(String projectName, List<Integer> userId) {
        Projects project = projectsRepository.findByProjectname(projectName);
        if (project == null || project.getStatus() != 2) return false;
        projectvalidationRepository.saveAll(userId.parallelStream().map(validatorId -> {
            Projectvalidation projectvalidation = new Projectvalidation();
            projectvalidation.setProjectid(project.getId());
            projectvalidation.setValidatorid(validatorId);
            projectvalidation.setAssignedtime(ServiceUtils.getNowTimeStamp());
            projectvalidation.setChecknumber(0);
            projectvalidation.setStatus(3);
            return projectvalidation;
        }).collect(Collectors.toList()));
        return true;
    }

}
