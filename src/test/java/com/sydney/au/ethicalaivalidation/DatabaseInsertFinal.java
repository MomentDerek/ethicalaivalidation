package com.sydney.au.ethicalaivalidation;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.mysql.cj.jdbc.MysqlDataSource;
import com.sydney.au.ethicalaivalidation.domain.*;
import com.sydney.au.ethicalaivalidation.repository.*;
import com.sydney.au.ethicalaivalidation.security.JwtTokenProvider;
import com.sydney.au.ethicalaivalidation.utils.ServiceUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;
import org.springframework.core.io.FileSystemResource;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.test.annotation.Rollback;
import org.springframework.util.DigestUtils;

import javax.sql.DataSource;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

@SpringBootTest(properties = {"spring.datasource.url=jdbc:mysql://localhost:3306/?serverTimezone=UTC&useUnicode=true&zeroDateTimeBehavior=convertToNull"})
@Rollback(false)
public class DatabaseInsertFinal {

    @Autowired
    private Environment environment;

    private final AnswerRepository answerRepository;
    private final CompanyRepository companyRepository;
    private final EthicalconcernsRepository ethicalconcernsRepository;
    private final PrinciplesRepository principlesRepository;
    private final ProjectassignRepository projectassignRepository;
    private final ProjectsRepository projectsRepository;
    private final ProjectvalidationRepository projectvalidationRepository;
    private final QuestionfeedbackRepository questionfeedbackRepository;
    private final QuestionsRepository questionsRepository;
    private final QuestionstatusRepository questionstatusRepository;
    private final QuestiontypeRepository questiontypeRepository;
    private final SegmentsRepository segmentsRepository;
    private final SegmentsummaryRepository segmentsummaryRepository;
    private final SubquestionsRepository subquestionsRepository;
    private final UsersRepository usersRepository;
    private final ValidatorfeedbackRepository validatorfeedbackRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private DataSource dataSource;

    @Autowired
    public DatabaseInsertFinal(UsersRepository usersRepository, ProjectassignRepository projectassignRepository, ProjectsRepository projectsRepository, CompanyRepository companyRepository, EthicalconcernsRepository ethicalconcernsRepository, ProjectvalidationRepository projectvalidationRepository, QuestionsRepository questionsRepository, SegmentsRepository segmentsRepository, QuestiontypeRepository questiontypeRepository, SubquestionsRepository subquestionsRepository, PrinciplesRepository principlesRepository, AnswerRepository answerRepository, ValidatorfeedbackRepository validatorfeedbackRepository, QuestionfeedbackRepository questionfeedbackRepository, JwtTokenProvider jwtTokenProvider, QuestionstatusRepository questionstatusRepository, SegmentsummaryRepository segmentsummaryRepository, DataSource dataSource) {
        this.usersRepository = usersRepository;
        this.projectassignRepository = projectassignRepository;
        this.projectsRepository = projectsRepository;
        this.companyRepository = companyRepository;
        this.ethicalconcernsRepository = ethicalconcernsRepository;
        this.projectvalidationRepository = projectvalidationRepository;
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
        this.dataSource = dataSource;
    }

    String Name1 = "Test";
    String Name2 = "Moment";

    List<Company> companyList = new ArrayList<>();
    List<Users> usersList = new ArrayList<>();
    List<Questiontype> questionTypeList = new ArrayList<>();

    @Test
    void test() throws Exception {
        deleteAll();
        MysqlDataSource mysqlDataSource = new MysqlDataSource();
        mysqlDataSource.setUrl("jdbc:mysql://localhost:3306/ethicalaivalidation?serverTimezone=UTC&useUnicode=true&zeroDateTimeBehavior=convertToNull");
        mysqlDataSource.setUser(environment.getProperty("spring.datasource.username"));
        mysqlDataSource.setUser(environment.getProperty("spring.datasource.password"));
        dataSource = mysqlDataSource;
        insertCompanys();
        insertUsers();
        insertQuestionsByJson();
    }

    @Test
    void insertQuestionsByJson() throws Exception {
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

        List<Answer> answerList = new ArrayList<>();

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
                        int type = questionTypeList.get(subQuestionJson.getInteger("type")).getType();
                        subQuestion.setQuestiontype(type);
                        System.out.println(subQuestion);
                        subquestionsRepository.save(subQuestion);
                        if (type == 1) {
                            answerList.add(new Answer(subQuestion.getId(), 1, 0));
                        }
                    }
                }
            }
        }

        answerRepository.saveAll(answerList);
    }

    @Test
    void deleteAll() {
        ResourceDatabasePopulator resourceDatabasePopulator = new ResourceDatabasePopulator();
        resourceDatabasePopulator.addScript(new FileSystemResource("src/main/resources/database/database create statement.sql"));
        resourceDatabasePopulator.execute(dataSource);

    }

    @Test
    void insertCompanys() {
        for (int i = 0; i < 2; i++) {
            Company company = new Company();
            company.setCompanyname(Name1 + i + "Company");
            companyList.add(company);
        }
        companyRepository.saveAll(companyList);
        System.out.println("companyList: " + companyList);
    }

    @Test
    void insertUsers() {
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
        usersRepository.findAll().forEach(System.out::println);
        System.out.println("userList: " + usersList);
    }

    private void insertQuestionType() {
        questionTypeList.add(new Questiontype());
        questionTypeList.add(1, new Questiontype(1));
        questionTypeList.add(2, new Questiontype(2));
        questiontypeRepository.saveAll(questionTypeList);
    }
}
