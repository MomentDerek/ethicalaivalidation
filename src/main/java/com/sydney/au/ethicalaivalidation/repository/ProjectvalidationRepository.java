package com.sydney.au.ethicalaivalidation.repository;

import com.sydney.au.ethicalaivalidation.domain.Projectvalidation;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * @author: Xin Lin on 11/2/2020
 * @package: com.sydney.au.ethicalaivalidation.repository
 * @version: 1.0
 * <b>Description:</b>
 * <p></p>
 */
@Repository
public interface ProjectvalidationRepository extends CrudRepository<Projectvalidation, Integer> {

    Projectvalidation findByProjectidAndValidatorid(Integer projectId, Integer validatorId);

    @Query(nativeQuery = true, value = "update projectvalidation set status = ?2 where projectid = ?1")
    void updateStatusByProjectId(Integer projectId, Integer status);

    @Query(value = "update Projectvalidation p set p.checknumber=p.checknumber+1 where p.projectid = ?1 and p.validatorid = ?2")
    void addCheckNumberByProjectIdAndValidatorId(Integer projectId,Integer validatorId);
}
