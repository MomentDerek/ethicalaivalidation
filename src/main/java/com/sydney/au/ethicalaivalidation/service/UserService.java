package com.sydney.au.ethicalaivalidation.service;

import com.sydney.au.ethicalaivalidation.domain.Company;
import com.sydney.au.ethicalaivalidation.domain.Users;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @author: Xin Lin on 11/9/2020
 * @package: com.sydney.au.ethicalaivalidation.service
 * @version: 1.0
 * <b>Description:</b>
 * <p></p>
 */
@Service
public interface UserService {
    Users getUserByUsername(String username);
    List<Company> getAllCompanies();
    Integer signup(String username,
                   String email,
                   String password,
                   Integer company,
                   Integer usertype,
                   String firstname,
                   String lastname,
                   String address1,
                   String address2,
                   String phone);
    Integer findCompanyId(Integer companyname);
    Boolean checkUserExists(String username1, String username);
    Boolean checkEmailToken(String username, String token);
    List<String> checkUserRoles(String username);
    Integer getUserType(String username);
    Map<String, Object> findUserInfo(String username);
    Boolean updateUserInfo(String username,
                           String firstname,
                           String lastname,
                           String address1,
                           String address2,
                           String phone);
    Boolean updateUserInfoWithUsername(String username,
                                       String username1,
                                       String firstname,
                                       String lastname,
                                       String address1,
                                       String address2,
                                       String phone);
    Boolean checkUsernameAndEmail(String username, String email);
    String getUserEmail(String username);
    String getUserPasswordToken(String username);
    Boolean validateUserPasswordToken(String username, String token);
    Boolean updateUserPassword(String username, String password);
    Boolean updateUserImage(String username, String imagepath);
}
