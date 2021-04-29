package com.sydney.au.ethicalaivalidation.repository;

import com.sydney.au.ethicalaivalidation.domain.Subquestions;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
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

    List<Subquestions> findByIdNotIn(List<Integer> questionId);

    List<Subquestions> findByQuestionid(Integer questionId);

    Optional<Subquestions> findByQuestionidAndContent(Integer questionId, String content);

    @Modifying
    @Query(nativeQuery = true, value = "update subquestions set content = ?2 , questiontype = ?3 where id = ?1")
    void updateDetailById(Integer subQuestionId, String content, Integer type);
}
