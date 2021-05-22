package com.sydney.au.ethicalaivalidation;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.sydney.au.ethicalaivalidation.domain.*;
import com.sydney.au.ethicalaivalidation.repository.*;
import com.sydney.au.ethicalaivalidation.security.JwtTokenProvider;
import com.sydney.au.ethicalaivalidation.utils.ServiceUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.DigestUtils;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

@SpringBootTest
public class DatabaseInsertFinal {

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
    private final JwtTokenProvider jwtTokenProvider;
    private final QuestionstatusRepository questionstatusRepository;
    private final SegmentsummaryRepository segmentsummaryRepository;

    @Autowired
    public DatabaseInsertFinal(UsersRepository usersRepository, ProjectassignRepository projectassignRepository, ProjectsRepository projectsRepository, CompanyRepository companyRepository, EthicalconcernsRepository ethicalconcernsRepository, QuestionsRepository questionsRepository, SegmentsRepository segmentsRepository, QuestiontypeRepository questiontypeRepository, SubquestionsRepository subquestionsRepository, PrinciplesRepository principlesRepository, AnswerRepository answerRepository, ValidatorfeedbackRepository validatorfeedbackRepository, QuestionfeedbackRepository questionfeedbackRepository, JwtTokenProvider jwtTokenProvider, QuestionstatusRepository questionstatusRepository, SegmentsummaryRepository segmentsummaryRepository) {
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
        this.jwtTokenProvider = jwtTokenProvider;
        this.questionstatusRepository = questionstatusRepository;
        this.segmentsummaryRepository = segmentsummaryRepository;
    }

    String Name1 = "Test";
    String Name2 = "Moment";

    List<Company> companyList = new ArrayList<>();
    List<Users> usersList = new ArrayList<>();
    List<Questiontype> questionTypeList = new ArrayList<>();


    @Test
    void insertQuestionsByJson() throws Exception {
        deleteAll();
        insertQuestionType();
        InputStream jsonStream = this.getClass().getResourceAsStream("/data/data.json");
        assert jsonStream != null;
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(jsonStream));
        StringBuilder stringBuilder = new StringBuilder();
        String line;
        while ((line = bufferedReader.readLine()) != null) {
            stringBuilder.append(line);
        }
        String text = stringBuilder.toString();

        JSONArray principlesJson = JSONArray.parseArray(text);

        for (int principleIndex = 0; principleIndex < principlesJson.size(); principleIndex++) {
            JSONObject principleJson = principlesJson.getJSONObject(principleIndex);
            Principles principle = new Principles();
            principle.setPrinciplename(principleJson.getString("principleName"));
            principlesRepository.save(principle);
            int principleId = principle.getId();
            JSONArray segmentsJson = principleJson.getJSONArray("segments");
            for (int segmentIndex = 0; segmentIndex < segmentsJson.size(); segmentIndex++) {
                JSONObject segmentJson = segmentsJson.getJSONObject(segmentIndex);
                Segments segment = new Segments();
                segment.setPrincipleid(principleId);
                segment.setSegmentname(segmentJson.getString("segmentName"));
                segmentsRepository.save(segment);
                int segmentId = segment.getId();
                JSONArray questionsJson = segmentJson.getJSONArray("questions");
                for (int questionIndex = 0; questionIndex < questionsJson.size(); questionIndex++) {
                    JSONObject questionJson = questionsJson.getJSONObject(questionIndex);
                    Questions question = new Questions();
                    question.setSegmentid(segmentId);
                    question.setQuestioncontent(questionJson.getString("questionContent"));
                    questionsRepository.save(question);
                    System.out.println(question.getId());
                    int questionId = question.getId();
                    JSONArray subQuestionsJson = questionJson.getJSONArray("subQuestions");
                    for (int subQuesIndex = 0; subQuesIndex < subQuestionsJson.size(); subQuesIndex++) {
                        JSONObject subQuestionJson = subQuestionsJson.getJSONObject(subQuesIndex);
                        Subquestions subQuestion = new Subquestions();
                        subQuestion.setContent(subQuestionJson.getString("subQuestionContent"));
                        subQuestion.setLevel(subQuesIndex);
                        subQuestion.setQuestionid(questionId);
                        subQuestion.setCreatedtime(ServiceUtils.getNowTimeStamp());
                        subQuestion.setQuestiontype(questionTypeList.get(subQuestionJson.getInteger("type")).getId());
                        System.out.println(subQuestion);
                        subquestionsRepository.save(subQuestion);
                    }
                }
            }
        }
    }


    @Test
    void test() throws Exception {
        deleteAll();
        insertCompanys();
        insertUsers();
        insertQuestionsByJson();
    }

    @Test
    void deleteAll() {
        answerRepository.deleteAll();
        questionfeedbackRepository.deleteAll();
        questionstatusRepository.deleteAll();
        ethicalconcernsRepository.deleteAll();
        projectassignRepository.deleteAll();
        validatorfeedbackRepository.deleteAll();
        projectassignRepository.deleteAll();
        segmentsummaryRepository.deleteAll();
        subquestionsRepository.deleteAll();
        projectsRepository.deleteAll();
        questiontypeRepository.deleteAll();
        questionsRepository.deleteAll();
        usersRepository.deleteAll();
        companyRepository.deleteAll();
        segmentsRepository.deleteAll();
        principlesRepository.deleteAll();
    }

    private void insertCompanys() {
        for (int i = 0; i < 2; i++) {
            Company company = new Company();
            company.setCompanyname(Name1 + i + "Company");
            companyList.add(company);
        }
        companyRepository.saveAll(companyList);
        System.out.println("companyList: " + companyList);
    }

    private void insertUsers() {
        int index = 0;


        String salt = "emailsalt";
        String password = "password";

        //其他身份用户
        for (int type : new int[]{1, 3, 4}) {
            String username = Name1 + index;
            String emailtoken = DigestUtils.md5DigestAsHex((username + salt).getBytes());
            String passwordtoken = jwtTokenProvider.passwordToken(username);
            String savedpassword = jwtTokenProvider.savedPasswordToken(password);

            Users u = new Users();
            u.setUsername(username);
            u.setEmail(Name1 + index + "@gmail.com");
            u.setPassword(savedpassword);
            u.setCompanyid(companyList.get(0).getId());
            u.setUsertype(type);
            u.setFirstname(Name1 + index);
            u.setLastname(Name2);
            u.setAddress1("address1" + index + Name1);
            u.setAddress2("address2" + index + Name2);
            u.setPhone("" + index + index);
            u.setCreatedtime(ServiceUtils.getNowTimeStamp());
            u.setVerifiedemail(1);
            u.setPasswordtoken(passwordtoken);
            u.setEmailtoken(emailtoken);
            u.setImage("defaultimage.png");
            usersList.add(u);
            index++;
        }

        for (Company company : companyList) {
            for (int i = 0; i < 2; i++, index++) {
                String username = Name1 + index;

                String emailtoken = DigestUtils.md5DigestAsHex((username + salt).getBytes());
                String passwordtoken = jwtTokenProvider.passwordToken(username);
                String savedpassword = jwtTokenProvider.savedPasswordToken(password);

                Users u = new Users();
                u.setUsername(username);
                u.setEmail(Name1 + index + "@gmail.com");
                u.setPassword(savedpassword);
                u.setCompanyid(company.getId());
                u.setUsertype(2);
                u.setFirstname(Name1 + index);
                u.setLastname(Name2);
                u.setAddress1("address1" + index + Name1);
                u.setAddress2("address2" + index + Name2);
                u.setPhone("" + index + index);
                u.setCreatedtime(ServiceUtils.getNowTimeStamp());
                u.setVerifiedemail(1);
                u.setPasswordtoken(passwordtoken);
                u.setEmailtoken(emailtoken);
                u.setImage("defaultimage.png");
                usersList.add(u);
            }
        }

        usersRepository.saveAll(usersList);
        System.out.println("userList: " + usersList);
    }

    private void insertQuestionType() {
        questionTypeList.add(new Questiontype());
        questionTypeList.add(1, new Questiontype(1));
        questionTypeList.add(2, new Questiontype(2));
        questiontypeRepository.saveAll(questionTypeList);
    }
}