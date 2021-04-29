package com.sydney.au.ethicalaivalidation.repository;

import com.sydney.au.ethicalaivalidation.domain.Ethicalconcerns;
import io.swagger.models.auth.In;
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
public interface EthicalconcernsRepository extends CrudRepository<Ethicalconcerns, Integer> {

    List<Ethicalconcerns> findByProjectid(Integer projectId);

    List<Ethicalconcerns> findByProjectidAndFinished(Integer projectId, Integer finished);

    Ethicalconcerns findByProjectidAndSubquesid(Integer projectId, Integer subQuestionId);

    @Modifying
    @Query(nativeQuery = true, value = "update ethicalconcerns set answer = ?3, points=?4 where projectid = ?1 and subquesid = ?2")
    void updateAnswerAndPointsByProjectIdAndSubQuesId(Integer projectId,Integer subQuestionId,String answer,Integer points);

    @Modifying
    @Query(nativeQuery = true, value = "update ethicalconcerns set finished = ?3 where projectid = ?1 and subquesid = ?2")
    void updateFinishedByProjectIdAndSubquesid(Integer projectId, Integer subQuestionId, Integer finished);

    @Modifying
    @Query(nativeQuery = true, value = "update ethicalconcerns set finished = ?3 where projectid = ?1 and subquesid in (?2)")
    void updateFinishedByProjectIdAndSubquesidIn(Integer projectId, List<Integer> subQuestionId, Integer finished);

    void deleteByProjectidAndSubquesid(Integer projectId, Integer subQuesId);
}
