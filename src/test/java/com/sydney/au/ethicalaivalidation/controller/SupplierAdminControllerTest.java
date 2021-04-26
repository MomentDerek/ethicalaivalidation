package com.sydney.au.ethicalaivalidation.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;

import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class SupplierAdminControllerTest {

    @Autowired
    private TestRestTemplate restTemplate;

    private HttpEntity httpEntity;

    private void login() throws Exception {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        Map<String, String> mp = new TreeMap<>();
        mp.put("username", "Test0");
        mp.put("password", "password");
        System.out.println(mp.toString());
        ResponseEntity<Map> responseEntity = restTemplate.postForEntity("/login", mp, Map.class, headers);
        Map body = responseEntity.getBody();
        System.out.println(body);
        headers.add("Authorization", "Bearer " + body.get("jwttoken"));
        System.out.println(headers);
        httpEntity = new HttpEntity(headers);
    }

    @Test
    void postNewProject() throws Exception {
        login();
        TreeMap<String, Object> reqMap = new TreeMap<>();
        ArrayList<Integer> principleList = new ArrayList<>();
        principleList.add(17);
        principleList.add(18);
        principleList.add(19);
        reqMap.put("projectname", "22336666");
        reqMap.put("description", "2233666");
        reqMap.put("ethicalconcerns", principleList);
        ResponseEntity<String> responseEntity = restTemplate.exchange("/newproject", HttpMethod.POST, httpEntity, String.class, reqMap);
        System.out.println(responseEntity.getBody());
    }
}