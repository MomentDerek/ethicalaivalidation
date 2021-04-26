package com.sydney.au.ethicalaivalidation.service;

import java.util.List;
import java.util.Map;

public interface ValidatorService {

    List<Map<String, Object>> getProjectList(String userName);

    Map<String, Object> getProjectDetail(String projectName, String validatorName);

    boolean addComment(String projectName, String validatorName, Integer subQuestionId, String comment, Boolean pass);

    boolean addSummary(String projectName, String validatorName, Integer segmentId, String comment);

    boolean passProject(String projectName, String validatorName);

    boolean sendFeedback(String projectName, String validatorName);
}
