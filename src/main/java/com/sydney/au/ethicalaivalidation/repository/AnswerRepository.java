package com.sydney.au.ethicalaivalidation.repository;

import com.sydney.au.ethicalaivalidation.domain.Answer;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author: Xin Lin on 11/2/2020
 * @package: com.sydney.au.ethicalaivalidation.repository
 * @version: 1.0
 * <b>Description:</b>
 * <p></p>
 */
@Repository
public interface AnswerRepository extends CrudRepository<Answer, Integer> {

    List<Answer> findBySubquesid(Integer subQuestionId);

}
