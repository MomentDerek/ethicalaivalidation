package com.sydney.au.ethicalaivalidation.repository;

import com.sydney.au.ethicalaivalidation.domain.Answer;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
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

    @Modifying
    @Query(nativeQuery = true, value = "update answer set answer =?2 where subquesid = ?1")
    void updateAnswerBySubQuestionId(Integer subQuestionId, Integer answer);

    List<Answer> findBySubquesidIn(List<Integer> subQuestionIdList);
}
