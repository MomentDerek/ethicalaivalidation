package com.sydney.au.ethicalaivalidation.repository;

import com.sydney.au.ethicalaivalidation.domain.Validatorfeedback;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import javax.persistence.criteria.CriteriaBuilder;
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
public interface ValidatorfeedbackRepository extends CrudRepository<Validatorfeedback, Integer> {

    List<Validatorfeedback> findByProjectidIn(List<Integer> projectIdList);

    Validatorfeedback findFirstByProjectidAndValidatoridOrderByCheckindexDesc(Integer projectId, Integer validatorId);

    Validatorfeedback findByProjectidAndValidatoridAndSendtime(Integer projectId, Integer validatorId, Timestamp sendTime);

}
