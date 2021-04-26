package com.sydney.au.ethicalaivalidation.service;

import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.Map;


public interface SupplierService {

    List<Map<String, String>> listProject(String supplierName);

    Map<String, String> getQuestion(String projectName, String userName);

    Map<String, Object> getQuestionPage(String projectName, String userName, String questionId, String subquesId);

    Map<String, Object> postAnswer(String projectName,
                                   Integer questionId,
                                   Integer subQuestionId,
                                   Map<String,Object> answer);

    List<Map<String, Object>> getAllMessage(String userName);

    List<Map<String, Object>> getValidatorFeedback(String projectName,String validatorName, String feedbackTime);

    Map<String, Object> getAssignFeedback(String projectName);

}
