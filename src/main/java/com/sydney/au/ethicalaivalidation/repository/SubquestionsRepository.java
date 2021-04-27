package com.sydney.au.ethicalaivalidation.repository;

import com.sydney.au.ethicalaivalidation.domain.Subquestions;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * @author: Xin Lin on 11/2/2020
 * @package: com.sydney.au.ethicalaivalidation.repository
 * @version: 1.0
 * <b>Description:</b>
 * <p></p>
 */
@Repository
public interface SubquestionsRepository extends CrudRepository<Subquestions, Integer> {

    List<Subquestions> findByQuestionidIn(List<Integer> questionId);

    List<Subquestions> findByQuestionid(Integer questionId);


    Optional<Subquestions> findByQuestionidAndContent(Integer questionId, String content);
}
