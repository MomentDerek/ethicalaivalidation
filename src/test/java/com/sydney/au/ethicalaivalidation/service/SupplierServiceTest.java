package com.sydney.au.ethicalaivalidation.service;

import com.sydney.au.ethicalaivalidation.domain.Projects;
import com.sydney.au.ethicalaivalidation.repository.ProjectsRepository;
import com.sydney.au.ethicalaivalidation.utils.ServiceUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;


@SpringBootTest
class SupplierServiceTest {

    @Autowired
    private SupplierService supplierService;

    @Autowired
    private ProjectsRepository projectsRepository;

    @Test
    void listProject() {
        System.out.println(supplierService.listProject("Test0"));
    }

    @Test
    void getQuestion() {
        System.out.println(supplierService.getQuestion("123","moment2"));
    }

    @Test
    void getQuestionPage() {
    }

    @Test
    void testListProject() {
    }

    @Test
    void testGetQuestion() {
    }

    @Test
    void testGetQuestionPage() {
    }

    @Test
    void postAnswer() {
    }

    @Test
    void getAllMessage() {
        List<Map<String, Object>> res = supplierService.getAllMessage("Test0");
        System.out.println(res);
        System.out.println(new ResponseEntity<>(res, HttpStatus.OK));
    }

    @Test
    void getValidatorFeedback() {
    }

    @Test
    void getAssignFeedback() {
    }
}