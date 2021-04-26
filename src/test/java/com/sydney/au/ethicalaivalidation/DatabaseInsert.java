package com.sydney.au.ethicalaivalidation;

import com.sydney.au.ethicalaivalidation.domain.*;
import com.sydney.au.ethicalaivalidation.repository.*;
import com.sydney.au.ethicalaivalidation.security.JwtTokenProvider;
import com.sydney.au.ethicalaivalidation.utils.ServiceUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.DigestUtils;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
public class DatabaseInsert {

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

    @Autowired
    public DatabaseInsert(UsersRepository usersRepository, ProjectassignRepository projectassignRepository, ProjectsRepository projectsRepository, CompanyRepository companyRepository, EthicalconcernsRepository ethicalconcernsRepository, QuestionsRepository questionsRepository, SegmentsRepository segmentsRepository, QuestiontypeRepository questiontypeRepository, SubquestionsRepository subquestionsRepository, PrinciplesRepository principlesRepository, AnswerRepository answerRepository, ValidatorfeedbackRepository validatorfeedbackRepository, QuestionfeedbackRepository questionfeedbackRepository, JwtTokenProvider jwtTokenProvider) {
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
    }

    String Name1 = "Test";
    String Name2 = "Moment";

    List<Company> companyList = new ArrayList<>();
    List<Users> usersList = new ArrayList<>();

    List<Principles> principleList = new ArrayList<>();
    List<Segments> segmentList = new ArrayList<>();
    List<Questions> questionList = new ArrayList<>();
    List<Questiontype> questionTypeList = new ArrayList<>();
    List<Subquestions> subQuestionList = new ArrayList<>();
    List<Answer> answerList = new ArrayList<>();

    @Test
    void test() {
        deleteAll();
        insertCompanys();
        insertUsers();

        insertPrinciple();
        insertSegment();
        insertQuestion();
        insertQuestionType();
        insertSubQuestion();
        insertAnswer();
    }

    @Test
    void deleteAll() {
        usersRepository.deleteAll();
        companyRepository.deleteAll();

        answerRepository.deleteAll();
        subquestionsRepository.deleteAll();
        questionsRepository.deleteAll();
        questiontypeRepository.deleteAll();
        segmentsRepository.deleteAll();
        principlesRepository.deleteAll();
    }

    private void insertCompanys() {
        for (int i = 0; i < 5; i++) {
            Company company = new Company();
            company.setCompanyname(Name1 + i + "Company");
            companyList.add(company);
        }
        companyRepository.saveAll(companyList);
        System.out.println("companyList: " + companyList);
    }

    private void insertUsers() {
        int index = 0;
        for (Company company : companyList) {
            for (int i = 0; i < 5; i++, index++) {
                String salt = "emailsalt";

                String username = Name1 + index;
                String password = "password";

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

        index++;
        String salt = "emailsalt";

        String username = Name1 + index;
        String password = "password";

        String emailtoken = DigestUtils.md5DigestAsHex((username + salt).getBytes());
        String passwordtoken = jwtTokenProvider.passwordToken(username);
        String savedpassword = jwtTokenProvider.savedPasswordToken(password);

        Users u = new Users();
        u.setUsername(username);
        u.setEmail(Name1 + index + "@gmail.com");
        u.setPassword(savedpassword);
        u.setCompanyid(companyList.get(0).getId());
        u.setUsertype(1);
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

        usersRepository.saveAll(usersList);
        System.out.println("userList: " + usersList);
    }

    private void insertPrinciple() {
        principleList.add(new Principles("Transparency"));
        principleList.add(new Principles("Fairness"));
        principleList.add(new Principles("Accountability"));
        principleList.add(new Principles("Privacy"));

        principlesRepository.saveAll(principleList);
        System.out.println("principleList: " + principleList);
    }

    private void insertSegment() {
        for (Principles principle : principleList) {
            for (int i = 0; i < 5; i++) {
                segmentList.add(new Segments(principle.getId(), "Segment-" + principle.getPrinciplename() + i));
            }
        }
        segmentsRepository.saveAll(segmentList);
        System.out.println("segmentList: " + segmentList);
    }

    private void insertQuestion() {
        for (Segments segment : segmentList) {
            for (int i = 0; i < 3; i++) {
                questionList.add(new Questions(segment.getId(), "Quetion-" + segment.getSegmentname() + i));
            }
        }
        questionsRepository.saveAll(questionList);
        System.out.println("questionList: " + questionList);
    }

    private void insertQuestionType() {
        questionTypeList.add(new Questiontype(1));
        questionTypeList.add(new Questiontype(2));
        questiontypeRepository.saveAll(questionTypeList);
    }

    private void insertSubQuestion() {
        for (Questions question : questionList) {
            for (int i = 0; i < 5; i++) {
                Subquestions subquestion = new Subquestions();
                subquestion.setQuestionid(question.getId());
                subquestion.setContent("subquestion test" + i + question.getQuestioncontent());
                subquestion.setCreatedtime(ServiceUtils.getNowTimeStamp());
                subquestion.setLevel(i);
                subquestion.setQuestiontype(questionTypeList.get(i%2).getId());
                subQuestionList.add(subquestion);
            }
        }
        subquestionsRepository.saveAll(subQuestionList);
        System.out.println("subquestionsList: " + subQuestionList);
    }

    private void insertAnswer() {
        for (Subquestions subquestions : subQuestionList) {
            if (subquestions.getQuestiontype() == 2) continue;
            for (int i = 0; i < 3; i++) {
                answerList.add(new Answer(subquestions.getId(), i, i));
            }
        }
        answerRepository.saveAll(answerList);
        System.out.println("answerList: "+answerList);
    }
}
