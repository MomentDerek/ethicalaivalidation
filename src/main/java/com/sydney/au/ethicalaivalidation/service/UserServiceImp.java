package com.sydney.au.ethicalaivalidation.service;

import com.sydney.au.ethicalaivalidation.domain.Company;
import com.sydney.au.ethicalaivalidation.domain.Users;
import com.sydney.au.ethicalaivalidation.repository.CompanyRepository;
import com.sydney.au.ethicalaivalidation.repository.UsersRepository;
import com.sydney.au.ethicalaivalidation.security.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.sql.Timestamp;
import java.util.*;

/**
 * @author: Xin Lin on 11/9/2020
 * @package: com.sydney.au.ethicalaivalidation.service
 * @version: 1.0
 * <b>Description:</b>
 * <p></p>
 */
@Service
public class UserServiceImp implements UserService {
    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private AsynchronousService asynchronousService;

    private String salt = "emailsalt";

    @Override
    public Users getUserByUsername(String username) {
        Users u = this.usersRepository.findByUsername(username);
        return u;
    }

    @Override
    public List<Company> getAllCompanies() {
        Iterable<Company> companies = companyRepository.findAll();
        List<Company> responses = new ArrayList<>();
        for(Company c: companies){
            responses.add(c);
        }
        return responses;
    }

    /**
     *
     * @param username
     * @param email
     * @param password
     * @param company
     * @param usertype
     * @param firstname
     * @param lastname
     * @param address1
     * @param address2
     * @param phone
     * @return res
     * <b>Description: </b>
     * <p>1 -> success </p>
     * <p>2 -> fail </p>
     * <p>3 -> exists username</p>
     */
    @Override
    public Integer signup(String username, String email, String password, Integer company, Integer usertype, String firstname, String lastname, String address1, String address2, String phone) {
        // find company id
        int companyid = this.findCompanyId(company);
        if(companyid == -1){
            return 2;
        }

        // check whether user already exists (check username)
        Users us = this.usersRepository.findByUsername(username);
        boolean existUser = (us == null)? false: true;
        if(existUser){
            return 3;
        }

        Date now = new Date();
        Timestamp createdtime = new Timestamp(now.getTime());

        String emailtoken = DigestUtils.md5DigestAsHex((username+salt).getBytes());
        String passwordtoken = jwtTokenProvider.passwordToken(username);
        String savedpassword = jwtTokenProvider.savedPasswordToken(password);


        Users u = new Users();
        u.setUsername(username);
        u.setEmail(email);
        u.setPassword(savedpassword);
        u.setCompanyid(companyid);
        u.setUsertype(usertype);
        u.setFirstname(firstname);
        u.setLastname(lastname);
        u.setAddress1(address1);
        u.setAddress2(address2);
        u.setPhone(phone);
        u.setCreatedtime(createdtime);
        u.setVerifiedemail(1);
        u.setPasswordtoken(passwordtoken);
        u.setEmailtoken(emailtoken);
        u.setImage("defaultimage.png");

        try {
            usersRepository.save(u);
        }catch (IllegalArgumentException e){
            e.printStackTrace();
        }

        // send verify email
        asynchronousService.sendEmailVerifyEmail(username, email, emailtoken);

        return 1;
    }

    /**
     *
     * @param companyname
     * @return companyid
     */
    @Override
    public Integer findCompanyId(Integer companyname) {
        Company c = companyRepository.findById(companyname).get();
        int companyid = -1;
        if(!c.equals(null)){
            companyid = c.getId();
        }
        return companyid;
    }

    @Override
    public Boolean checkUserExists(String username1, String username) {
        Users u = usersRepository.findByUsername(username);
        Users u1 = usersRepository.findByUsername(username1);

        if(u1 == null) return false;
        if(u.getPassword().equals(u1.getPassword())) return false;
        return true;
    }

    @Override
    public Boolean checkEmailToken(String username, String token) {
        boolean res = false;
        Users u = usersRepository.findByUsername(username);
        if(!u.equals(null)){
            String emailToken = u.getEmailtoken();
            if(emailToken.equals(token)){
                u.setVerifiedemail(2);
                usersRepository.save(u);
                return true;
            }
        }
        return res;
    }

    @Override
    public List<String> checkUserRoles(String username) {
        List<String> roles = new ArrayList<>();
        List<Users> us = new ArrayList<>();
        if(usersRepository.findByUsername(username) != null){
            Users u = usersRepository.findByUsername(username);
            int type = 0;
            type = u.getUsertype();
            if(type == 1){
                roles.add("ROLE_"+"AI SupplierAdmin");
            }
            else if(type == 2){
                roles.add("ROLE_"+"AI Supplier");
            }
            else if(type == 3){
                roles.add("ROLE_"+"Validator");
            }
            else {
                roles.add("ROLE_"+"Administrator");
            }
        }
        return roles;
    }

    @Override
    public Integer getUserType(String username) {
        Users u = this.usersRepository.findByUsername(username);
        int usertype = u.getUsertype();
        return usertype;
    }

    @Override
    public Map<String, Object> findUserInfo(String username) {
        Map<String, Object> m = new TreeMap<>();
        Users u = this.usersRepository.findByUsername(username);
        int companyid = u.getCompanyid();
        Company c = this.companyRepository.findById(companyid).get();
        m.put("email", u.getEmail());
        m.put("username", u.getUsername());
        m.put("company", c.getCompanyname());
        m.put("usertype", u.getUsertype());
        m.put("firstname", u.getFirstname());
        m.put("lastname", u.getLastname());
        m.put("address1", u.getAddress1());
        m.put("address2", u.getAddress2());
        m.put("phone", u.getPhone());
        m.put("image", u.getImage());
        m.put("verifiedemail", u.getVerifiedemail());
        return m;
    }

    @Override
    public Boolean updateUserInfo(String username, String firstname, String lastname, String address1, String address2, String phone) {
        boolean check = false;
        Users u = this.usersRepository.findByUsername(username);
        try {
            u.setFirstname(firstname);
            u.setLastname(lastname);
            u.setAddress1(address1);
            u.setAddress2(address2);
            u.setPhone(phone);
            this.usersRepository.save(u);
            check = true;
        }catch (Exception e){
            e.printStackTrace();
        }

        return check;
    }

    @Override
    public Boolean updateUserInfoWithUsername(String username, String username1, String firstname, String lastname, String address1, String address2, String phone) {
        boolean check = false;
        Users u = this.usersRepository.findByUsername(username);
        try {
            u.setUsername(username1);
            u.setFirstname(firstname);
            u.setLastname(lastname);
            u.setAddress1(address1);
            u.setAddress2(address2);
            u.setPhone(phone);
            this.usersRepository.save(u);
            check = true;
        }catch (Exception e){
            e.printStackTrace();
        }
        return check;
    }

    @Override
    public Boolean checkUsernameAndEmail(String username, String email) {
        boolean check = false;
        Users u = this.usersRepository.findByUsername(username);
        if(u == null) return check;
        String uemail = u.getEmail();

        if(uemail.equals(email)) check=true;
        return check;
    }

    @Override
    public String getUserEmail(String username) {
        String email = "";
        Users u = this.usersRepository.findByUsername(username);
        if (u != null){
            email = u.getEmail();
        }
//        email = this.usersRepository.findEmailByUsername(username);
        if(email.equals(null)) email = "";
        return email;
    }

    @Override
    public String getUserPasswordToken(String username) {
        String passwordToken = "";
        Users u = this.usersRepository.findByUsername(username);
        if(u == null) return passwordToken;

        passwordToken = u.getPasswordtoken();
        return passwordToken;
    }

    @Override
    public Boolean validateUserPasswordToken(String username, String token) {
        Boolean check = false;
        Users u = this.usersRepository.findByUsername(username);
        if(u == null) return check;

        String passwordToken = u.getPasswordtoken();
        if(passwordToken.equals(token)){
            check = true;
        }
        return check;
    }

    @Override
    public Boolean updateUserPassword(String username, String password) {
        boolean check = false;
        Users u = this.usersRepository.findByUsername(username);
        if(u == null) return check;
        try {
            String newPasswordToken = jwtTokenProvider.passwordToken(username);
            String newPassword = jwtTokenProvider.savedPasswordToken(password);
            u.setPassword(newPassword);
            u.setPasswordtoken(newPasswordToken);
            this.usersRepository.save(u);
            check = true;
        }catch (Exception e){
            e.printStackTrace();
        }
        return check;
    }

    @Override
    public Boolean updateUserImage(String username, String imagepath) {
        boolean check = false;
        Users u = this.usersRepository.findByUsername(username);
        if(u == null) return check;
        try {
            u.setImage(imagepath);
            this.usersRepository.save(u);
            check = true;
        }catch (Exception e){
            e.printStackTrace();
        }
        return check;
    }


}
