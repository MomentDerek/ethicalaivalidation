package com.sydney.au.ethicalaivalidation.repository;

import com.sydney.au.ethicalaivalidation.domain.Questionfeedback;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.List;

/**
 * @author: Xin Lin on 11/2/2020
 * @package: com.sydney.au.ethicalaivalidation.repository
 * @version: 1.0
 * <b>Description:</b>
 * <p></p>
 */
@Repository
public interface QuestionfeedbackRepository extends CrudRepository<Questionfeedback, Integer> {

    List<Questionfeedback> findByProjectidAndValidatoridAndCreatedindex
            (Integer projectId, Integer validatorId, Integer createdIndex);

    List<Questionfeedback> findByProjectidAndValidatoridAndSubquesid(Integer projectId, Integer validatorId, Integer subQuesId);

    List<Questionfeedback> findByProjectidAndSubquesid(Integer projectId, Integer subQuesId);

    @Modifying
    @Query(nativeQuery = true, value = "update questionfeedback set createdindex = ?4 ,content = ?5, feedbacktime=?6 where projectid = ?1 and validatorid = ?2 and subquesid = ?3")
    void updateByProjectIdAndValidatorIdAndSubquesid(Integer projectId, Integer validatorId, Integer subQuestionId, Integer createIndex, String comment, Timestamp feedbackTime);

    List<Questionfeedback> findByProjectidAndValidatorid(Integer projectId, Integer validatorId);

}
