package com.sydney.au.ethicalaivalidation.repository;

import com.sydney.au.ethicalaivalidation.domain.Questionstatus;
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
public interface QuestionstatusRepository extends CrudRepository<Questionstatus, Integer> {

    List<Questionstatus> findByProjectidAndSubquesidIn(Integer projectId, List<Integer> subQuestionIds);

    Optional<Questionstatus> findByProjectidAndValidatoridAndSubquesid(Integer projectId, Integer validatorId, Integer subQuestionId);

    @Modifying
    @Query(nativeQuery = true, value = "update questionstatus set status = ?4 where projectid = ?1 and validatorid = ?2 and subquesid = ?3")
    void updateByProjectIdAndValidatorIdAndSubQuestionId(Integer projectId, Integer validatorId, Integer subQuestionId, Integer status);

    @Modifying
    @Query(nativeQuery = true, value = "update questionstatus set status = ?2 where projectid = ?1")
    void updateByProjectId(Integer projectId, Integer status);
}
