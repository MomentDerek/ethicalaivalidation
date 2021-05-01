package com.sydney.au.ethicalaivalidation.repository;

import com.sydney.au.ethicalaivalidation.utils.ServiceUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
class QuestionfeedbackRepositoryTest {

    @Autowired
    private QuestionfeedbackRepository questionfeedbackRepository;

    @Test
    @Transactional
    void updateByProjectIdAndValidatorIdAndSubquesid() {
        questionfeedbackRepository.updateByProjectIdAndValidatorIdAndSubquesid(1, 1, 1, 3, "2323", ServiceUtils.getNowTimeStamp());
        System.out.println(questionfeedbackRepository.findByProjectidAndValidatoridAndSubquesid(1, 1, 2).isEmpty());
    }
}