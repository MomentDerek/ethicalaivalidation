package com.sydney.au.ethicalaivalidation.service;

import java.util.List;
import java.util.Map;

public interface SupplierAdminService {

    Boolean isUserCreateProject(String userName, String projectName);

    List<Map<String, Object>> getProject(String userName);

    List<Map<String, Object>> getPrinciple();

    Boolean newProject(String userName,String projectName, String description,List<Integer> principles);

    Map<String, Object> getProjectInfo(String projectName);

    List<Map<String, Object>> getAllSupplier();

    Boolean postAssignSupplier(String projectName, List<Integer> supplierList);

}
