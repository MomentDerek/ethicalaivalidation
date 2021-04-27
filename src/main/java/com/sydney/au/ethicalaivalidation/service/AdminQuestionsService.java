package com.sydney.au.ethicalaivalidation.service;

import java.util.List;
import java.util.Map;

public interface AdminQuestionsService {

    List<Map<String, Object>> listAllQuestion();

    List<Map<String, Object>> listAllPrinciple();

    List<Map<String, Object>> listSegmentByPrinciple(Integer principleId);

    List<Map<String, Object>> listQuestionBySegment(Integer segmentId);

    boolean addPrinciple(String principleName);

    boolean addSegment(Integer principleId, String SegmentName);

    boolean addQuestion(Integer segmentId, String questionContent);

    boolean addSubQuestion(Integer questionId, Integer questionTypeId, String SubQuestionContent, Integer answer);

    List<Map<String, Object>> listAllQuestionType();

    Map<String, Object> getSubQuestionDetail(Integer subQuestionId);

    boolean updateSubQuestion(Integer principleId,
                              Integer segmentId,
                              Integer questionId,
                              Integer subQuestionId,
                              String subQuestionContent,
                              Integer questionType,
                              Integer answer);

    boolean deleteSubQuestion(Integer subQuestionId);
}
