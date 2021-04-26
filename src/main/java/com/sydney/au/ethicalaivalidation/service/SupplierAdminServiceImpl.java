package com.sydney.au.ethicalaivalidation.service;

import com.sydney.au.ethicalaivalidation.domain.*;
import com.sydney.au.ethicalaivalidation.repository.*;
import com.sydney.au.ethicalaivalidation.utils.ServiceUtils;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class SupplierAdminServiceImpl implements SupplierAdminService {

    private final UsersRepository usersRepository;
    private final ProjectassignRepository projectassignRepository;
    private final ProjectsRepository projectsRepository;
    private final CompanyRepository companyRepository;
    private final EthicalconcernsRepository ethicalconcernsRepository;
    private final QuestionsRepository questionsRepository;
    private final SegmentsRepository segmentsRepository;
    private final SubquestionsRepository subquestionsRepository;
    private final PrinciplesRepository principlesRepository;

    public SupplierAdminServiceImpl(UsersRepository usersRepository, ProjectassignRepository projectassignRepository, ProjectsRepository projectsRepository, CompanyRepository companyRepository, EthicalconcernsRepository ethicalconcernsRepository, QuestionsRepository questionsRepository, SegmentsRepository segmentsRepository, QuestiontypeRepository questiontypeRepository, SubquestionsRepository subquestionsRepository, PrinciplesRepository principlesRepository, AnswerRepository answerRepository, ValidatorfeedbackRepository validatorfeedbackRepository, QuestionfeedbackRepository questionfeedbackRepository) {
        this.usersRepository = usersRepository;
        this.projectassignRepository = projectassignRepository;
        this.projectsRepository = projectsRepository;
        this.companyRepository = companyRepository;
        this.ethicalconcernsRepository = ethicalconcernsRepository;
        this.questionsRepository = questionsRepository;
        this.segmentsRepository = segmentsRepository;
        this.subquestionsRepository = subquestionsRepository;
        this.principlesRepository = principlesRepository;
    }

    @Override
    public Boolean isUserCreateProject(String userName, String projectName) {
        Projects project = projectsRepository.findByProjectname(projectName);
        Users user = usersRepository.findByUsername(userName);
        return project.getCreatorid().equals(user.getId());
    }

    @Override
    public List<Map<String, Object>> getProject(String userName) {
        Users supplierAdmin = usersRepository.findByUsername(userName);
        List<Projects> projectList = projectsRepository.findByCreatorid(supplierAdmin.getId());
        List<Map<String, Object>> res = new ArrayList<>();
        projectList.forEach(x -> {
            Map<String, Object> project = new TreeMap<>();
            project.put("projectid", x.getId());
            project.put("projectname", x.getProjectname());
            project.put("createdtime", x.getCreatedtime());
            project.put("status", x.getStatus());
            res.add(project);
        });
        return res;
    }

    @Override
    public List<Map<String, Object>> getPrinciple() {
        Iterable<Principles> principles = principlesRepository.findAll();
        List<Map<String, Object>> res = new ArrayList<>();
        principles.forEach(x -> {
            Map<String, Object> principle = new TreeMap<>();
            principle.put("priciplesid", x.getId());
            principle.put("priciplesname", x.getPrinciplename());
            res.add(principle);
        });
        return res;
    }

    @Override
    public Boolean newProject(String userName, String projectName, String description, List<Integer> principles) {
        Users user = usersRepository.findByUsername(userName);
        Projects project = projectsRepository.findByProjectname(projectName);
        if (project != null) return false;
        //插入project
        project = new Projects();
        project.setProjectname(projectName);
        project.setDescription(description);
        project.setCreatorid(user.getId());
        project.setCreatedtime(ServiceUtils.getNowTimeStamp());
        project.setStatus(1);
        Projects saveProject = projectsRepository.save(project);
        List<Segments> segmentsList = segmentsRepository.findByPrincipleidIn(principles);
        List<Questions> questionsList = questionsRepository.findBySegmentidIn(
                segmentsList.stream().map(Segments::getId).collect(Collectors.toList()));
        List<Subquestions> subquestionsList = subquestionsRepository.findByQuestionidIn(
                questionsList.stream().map(Questions::getId).collect(Collectors.toList()));
        List<Ethicalconcerns> ethicalconcernsList = new ArrayList<>();
        for (Subquestions subQuestion : subquestionsList) {
            Ethicalconcerns ethicalconcerns = new Ethicalconcerns();
            ethicalconcerns.setProjectid(saveProject.getId());
            ethicalconcerns.setSubquesid(subQuestion.getId());
            ethicalconcerns.setQuestionid(subQuestion.getQuestionid());
            ethicalconcerns.setFinished(1);
            ethicalconcernsList.add(ethicalconcerns);
        }
        ethicalconcernsRepository.saveAll(ethicalconcernsList);
        return true;
    }

    @Override
    public Map<String, Object> getProjectInfo(String projectName) {
        Projects project = projectsRepository.findByProjectname(projectName);
        List<Principles> principleList = principlesRepository.getPrincipleByProjectId(project.getId());

        Map<String, Object> res = new TreeMap<>();
        res.put("projectname", project.getProjectname());
        res.put("description", project.getDescription());
        res.put("ethicalconcerns", principleList.stream().map(x -> {
            Map<String, Object> map = new TreeMap<>();
            map.put("principleid", x.getId());
            map.put("principlename", x.getPrinciplename());
            return map;
        }).collect(Collectors.toList()));
        return res;
    }

    @Override
    public List<Map<String, Object>> getAllSupplier() {
        List<Users> userList = usersRepository.findByUsertype(2);
        Map<Integer, String> companyMap = new HashMap<>();
        companyRepository.findAllById(userList.stream().map(Users::getCompanyid).collect(Collectors.toList()))
                .forEach(x -> companyMap.put(x.getId(), x.getCompanyname()));
        return userList.stream().map(user -> {
            Map<String, Object> userMap = new TreeMap<>();
            userMap.put("supplierid", user.getId());
            userMap.put("username", user.getUsername());
            userMap.put("company", companyMap.get(user.getCompanyid()));
            return userMap;
        }).collect(Collectors.toList());
    }

    @Override
    public Boolean postAssignSupplier(String projectName, List<Integer> supplierList) {
        Projects project = projectsRepository.findByProjectname(projectName);
        //分配项目
        List<Projectassign> projectassignList = supplierList.stream().map(x -> {
            Projectassign projectassign = new Projectassign();
            projectassign.setProjectid(project.getId());
            projectassign.setSupplierid(x);
            projectassign.setAssigntime(ServiceUtils.getNowTimeStamp());
            projectassign.setLocked(1);
            projectassign.setUpdatetime(ServiceUtils.getNowTimeStamp());
            return projectassign;
        }).collect(Collectors.toList());
        projectassignRepository.saveAll(projectassignList);
        //将分配的项目的状态设置为已分配
        project.setStatus(2);
        projectsRepository.save(project);
        return true;
    }
}
