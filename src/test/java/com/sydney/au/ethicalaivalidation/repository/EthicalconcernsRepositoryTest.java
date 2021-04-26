package com.sydney.au.ethicalaivalidation.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;


@SpringBootTest
class EthicalconcernsRepositoryTest {

    @Autowired
    private EthicalconcernsRepository ethicalconcernsRepository;

    @Test
    void findByProjectid() {
        ArrayList<Integer> list = new ArrayList<>();
        list.add(1);
        System.out.println(ethicalconcernsRepository.findByProjectidAndFinished(1,1));
    }
}