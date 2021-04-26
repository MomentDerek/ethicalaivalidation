package com.sydney.au.ethicalaivalidation.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.annotation.AuthenticationPrincipal;


@SpringBootTest
class PrinciplesRepositoryTest {
    @Autowired
    private PrinciplesRepository principlesRepository;

    @Test
    void getPrincipleByProjectId() {
        System.out.println(principlesRepository.getPrincipleByProjectId(1));
    }
}