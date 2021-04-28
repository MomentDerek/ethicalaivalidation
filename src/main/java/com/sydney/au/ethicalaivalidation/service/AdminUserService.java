package com.sydney.au.ethicalaivalidation.service;

import java.util.List;
import java.util.Map;

public interface AdminUserService {

    List<Map<String, Object>> listAllUser();

    List<Map<String, Object>> listAllUserByType(String type);

    Map<String, Object> getUserDetail(String userName);

    boolean deleteUser(String userName);
}
