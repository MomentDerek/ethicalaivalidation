package com.sydney.au.ethicalaivalidation.repository;

import com.sydney.au.ethicalaivalidation.domain.Ethicalconcerns;
import io.swagger.models.auth.In;
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

    Ethicalconcerns findByProjectidAndSubquesid(Integer projectId,Integer subQuestionId);

    @Query(nativeQuery = true,value = "update ethicalconcerns set finished = ?3 where projectid = ?1 and subquesid in (?2)")
    void updateFinishedByProjectIdAndSubquesid(Integer projectId, List<Integer> subQuestionId,Integer finished);
}
