package com.sydney.au.ethicalaivalidation.service;

import java.util.List;
import java.util.Map;

public interface AdminProjectService {
    List<Map<String, Object>> listAllProject();

    Map<String, Object> getProjectDetail(String projectName);

    List<Map<String, Object>> listOtherQuestion(String projectName);

    boolean addOtherQuestion(String projectName, Integer projectId, Integer subQuesId);

    boolean deleteQuestion(String projectName, Integer subQuesId);

    List<Map<String, Object>> listAllValidator(String projectName);

    boolean assignValidator(String projectName, List<Integer> userId);
}
