package com.sydney.au.ethicalaivalidation.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ProjectassignRepositoryTest {

    @Autowired
    private ProjectassignRepository projectassignRepository;

    @Test
    void findDistinctBySupplierid(){
        System.out.println(projectassignRepository.findBySupplierid(2).toString());
    }

    @Test
    void findDistinctByProjectid() {
        System.out.println(projectassignRepository.findByProjectid(1));
    }
}