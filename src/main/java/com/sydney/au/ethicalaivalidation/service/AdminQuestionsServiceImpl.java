package com.sydney.au.ethicalaivalidation.service;

import com.sydney.au.ethicalaivalidation.domain.*;
import com.sydney.au.ethicalaivalidation.repository.*;
import com.sydney.au.ethicalaivalidation.utils.ServiceUtils;
import org.springframework.stereotype.Service;
import sun.jvm.hotspot.oops.Oop;

import java.util.*;

@Service
public class AdminQuestionsServiceImpl implements AdminQuestionsService {

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

    public AdminQuestionsServiceImpl(UsersRepository usersRepository, ProjectassignRepository projectassignRepository, ProjectsRepository projectsRepository, CompanyRepository companyRepository, EthicalconcernsRepository ethicalconcernsRepository, QuestionsRepository questionsRepository, SegmentsRepository segmentsRepository, QuestiontypeRepository questiontypeRepository, SubquestionsRepository subquestionsRepository, PrinciplesRepository principlesRepository, AnswerRepository answerRepository, ValidatorfeedbackRepository validatorfeedbackRepository, QuestionfeedbackRepository questionfeedbackRepository, ProjectvalidationRepository projectvalidationRepository, SegmentsummaryRepository segmentsummaryRepository, QuestionstatusRepository questionstatusRepository) {
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
    public List<Map<String, Object>> listAllQuestion() {
        List<Principles> principles = new ArrayList<>();
        principlesRepository.findAll().forEach(principles::add);
        List<Segments> segments = new ArrayList<>();
        segmentsRepository.findAll().forEach(segments::add);
        List<Questions> questions = new ArrayList<>();
        questionsRepository.findAll().forEach(questions::add);
        List<Subquestions> subquestions = new ArrayList<>();
        subquestionsRepository.findAll().forEach(subquestions::add);
        //构建结果
        List<Map<String, Object>> res = new ArrayList<>();
        principles.parallelStream().forEach(principle -> {
            Map<String, Object> principleMap = new TreeMap<>();
            principleMap.put("principleid", principle.getId());
            principleMap.put("principle", principle.getPrinciplename());
            List<Map<String, Object>> principleContent = new ArrayList<>();
            segments.parallelStream().forEach(segment -> {
                if (segment.getPrincipleid() != principle.getId()) return;
                Map<String, Object> segmentMap = new TreeMap<>();
                segmentMap.put("segmentid", segment.getId());
                segmentMap.put("segment", segment.getSegmentname());
                List<Map<String, Object>> segmentContent = new ArrayList<>();
                questions.parallelStream().forEach(question -> {
                    if (question.getSegmentid() != segment.getId()) return;
                    Map<String, Object> questionMap = new TreeMap<>();
                    questionMap.put("questionid", question.getId());
                    questionMap.put("question", question.getQuestioncontent());
                    List<Map<String, Object>> questionContent = new ArrayList<>();
                    subquestions.parallelStream().forEach(subQuestion -> {
                        if (subQuestion.getQuestionid() != question.getId()) return;
                        Map<String, Object> subQuestionMap = new TreeMap<>();
                        subQuestionMap.put("subquesid", subQuestion.getId());
                        subQuestionMap.put("subquestion", subQuestion.getContent());
                        subQuestionMap.put("questiontype", subQuestion.getQuestiontype());
                        questionContent.add(subQuestionMap);
                    });
                    questionMap.put("questioncontent", questionContent);
                    segmentContent.add(questionMap);
                });
                segmentMap.put("segmentcontent", segmentContent);
                principleContent.add(segmentMap);
            });
            principleMap.put("principlecontent", principleContent);
            res.add(principleMap);
        });
        return res;
    }

    @Override
    public List<Map<String, Object>> listAllPrinciple() {
        List<Principles> principles = new ArrayList<>();
        principlesRepository.findAll().forEach(principles::add);
        //构建结果
        List<Map<String, Object>> res = new ArrayList<>();
        principles.parallelStream().forEach(principle -> {
            Map<String, Object> principleMap = new TreeMap<>();
            principleMap.put("principleid", principle.getId());
            principleMap.put("principlename", principle.getPrinciplename());
            res.add(principleMap);
        });
        return res;
    }

    @Override
    public List<Map<String, Object>> listSegmentByPrinciple(Integer principleId) {
        List<Segments> segments = new ArrayList<>(segmentsRepository.findByPrincipleid(principleId));
        List<Map<String, Object>> res = new ArrayList<>();
        segments.parallelStream().forEach(segment -> {
            Map<String, Object> segmentMap = new TreeMap<>();
            segmentMap.put("segmentid", segment.getId());
            segmentMap.put("segmentname", segment.getSegmentname());
            res.add(segmentMap);
        });
        return res;
    }

    @Override
    public List<Map<String, Object>> listQuestionBySegment(Integer segmentId) {
        List<Questions> questions = new ArrayList<>(questionsRepository.findBySegmentid(segmentId));
        List<Map<String, Object>> res = new ArrayList<>();
        questions.parallelStream().forEach(question -> {
            Map<String, Object> questionMap = new TreeMap<>();
            questionMap.put("questionid", question.getId());
            questionMap.put("questioncontent", question.getQuestioncontent());
            res.add(questionMap);
        });
        return res;
    }

    @Override
    public boolean addPrinciple(String principleName) {
        if (principlesRepository.findByPrinciplename(principleName).isPresent()) {
            return false;
        }
        principlesRepository.save(new Principles(principleName));
        return true;
    }

    @Override
    public boolean addSegment(Integer principleId, String SegmentName) {
        if (segmentsRepository.findByPrincipleidAndSegmentname(principleId, SegmentName).isPresent()) {
            return false;
        }
        segmentsRepository.save(new Segments(principleId, SegmentName));
        return true;
    }

    @Override
    public boolean addQuestion(Integer segmentId, String questionContent) {
        if (questionsRepository.findBySegmentidAndAndQuestioncontent(segmentId, questionContent).isPresent()) {
            return false;
        }
        questionsRepository.save(new Questions(segmentId, questionContent));
        return true;
    }

    @Override
    public boolean addSubQuestion(Integer questionId, Integer questionTypeId, String SubQuestionContent, Integer answer) {
        if (subquestionsRepository.findByQuestionidAndContent(questionId, SubQuestionContent).isPresent()) {
            return false;
        }
        int level = subquestionsRepository.findByQuestionid(questionId)
                .stream().max((a, b) -> a.getLevel() > b.getLevel() ? 1 : -1).get()
                .getLevel() + 1;
        Subquestions newSubQuestion = new Subquestions(
                questionId,
                SubQuestionContent,
                questionTypeId,
                ServiceUtils.getNowTimeStamp(),
                level);
        subquestionsRepository.save(newSubQuestion);
        answerRepository.save(new Answer(newSubQuestion.getId(), answer, 1));
        return true;
    }

    @Override
    public List<Map<String, Object>> listAllQuestionType() {
        List<Questiontype> types = new ArrayList<>();
        questiontypeRepository.findAll().forEach(types::add);
        //构建结果
        List<Map<String, Object>> res = new ArrayList<>();
        types.parallelStream().forEach(type -> {
            Map<String, Object> typeMap = new TreeMap<>();
            typeMap.put("questiontypeid", type.getId());
            typeMap.put("questiontype", type.getType());
            res.add(typeMap);
        });
        return res;
    }

    @Override
    public Map<String, Object> getSubQuestionDetail(Integer subQuestionId) {
        Optional<Subquestions> subQuestionOptional = subquestionsRepository.findById(subQuestionId);
        if (!subQuestionOptional.isPresent()) return new TreeMap<>();
        Subquestions subQuestion = subQuestionOptional.get();
        int questionId = subQuestion.getQuestionid();
        Questions question = questionsRepository.findById(questionId).get();
        int segmentId = question.getSegmentid();
        Segments segment = segmentsRepository.findById(segmentId).get();
        int principleId = segment.getPrincipleid();
        Principles principle = principlesRepository.findById(principleId).get();
        //构建结果
        Map<String, Object> res = new TreeMap<>();
        res.put("principleid", principleId);
        res.put("principle", principle.getPrinciplename());
        res.put("segmentid", segmentId);
        res.put("segment", segment.getSegmentname());
        res.put("questionid", questionId);
        res.put("question", question.getQuestioncontent());
        res.put("subquescontent", subQuestion.getContent());
        res.put("questiontype", questiontypeRepository.findById(subQuestion.getQuestiontype()).get());
        res.put("answer", answerRepository.findBySubquesid(subQuestionId));
        res.put("createdtime", subQuestion.getCreatedtime());
        return res;
    }

    @Override
    public boolean updateSubQuestion(Integer principleId, Integer segmentId, Integer questionId, Integer subQuestionId, String subQuestionContent, Integer questionType, Integer answer) {
        Optional<Subquestions> subQuestionOpt = subquestionsRepository.findById(subQuestionId);
        if (!subQuestionOpt.isPresent()) return false;
        //TODO 此处可加principleID，segmentID，QuestionID的验证
        try {
            subquestionsRepository.updateDetailById(subQuestionId, subQuestionContent, questionType);
            answerRepository.updateAnswerBySubQuestionId(subQuestionId, answer);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    @Override
    public boolean deleteSubQuestion(Integer subQuestionId) {
        Optional<Subquestions> subQuestionOpt = subquestionsRepository.findById(subQuestionId);
        if (!subQuestionOpt.isPresent()) return false;
        subquestionsRepository.deleteById(subQuestionId);
        return true;
    }


}
