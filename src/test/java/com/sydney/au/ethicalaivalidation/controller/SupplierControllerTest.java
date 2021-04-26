package com.sydney.au.ethicalaivalidation.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;

import java.util.Map;
import java.util.TreeMap;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class SupplierControllerTest {

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
        ResponseEntity<Map> responseEntity = restTemplate.postForEntity("/login", mp, Map.class,headers);
        Map body = responseEntity.getBody();
        System.out.println(body);
        headers.add("Authorization","Bearer "+body.get("jwttoken"));
        System.out.println(headers);
        httpEntity = new HttpEntity(headers);
    }

    @Test
    void getProjectList() {

    }

    @Test
    void getQuestion() {
    }

    @Test
    void getQuestionPage() {
    }

    @Test
    void postAnswer() {
    }

    @Test
    void getAllMessage() throws Exception {
        login();
        ResponseEntity<String> responseEntity = restTemplate
                .exchange("/feedback", HttpMethod.GET, httpEntity,String.class,new TreeMap<>());
        System.out.println(responseEntity.getBody());
    }

    @Test
    void getValidatorFeedback() {
    }

    @Test
    void getAssignFeedback() {
    }

    @Test
    void getReport() {
    }
}