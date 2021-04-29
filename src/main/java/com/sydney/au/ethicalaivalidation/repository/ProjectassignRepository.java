package com.sydney.au.ethicalaivalidation.repository;

import com.sydney.au.ethicalaivalidation.domain.Projectassign;
import com.sydney.au.ethicalaivalidation.domain.Projects;
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
public interface ProjectassignRepository extends CrudRepository<Projectassign, Integer> {

    List<Projectassign> findBySupplierid(Integer supplierId);

    List<Projectassign> findByProjectid(Integer projectId);

    @Modifying
    @Query(nativeQuery = true, value = "update projectassign set locked = 1 where projectid = ?1 and supplierid = ?2")
    void unLockByProjectIdAndSupplierId(Integer projectId, Integer supplierId);

    @Modifying
    @Query(nativeQuery = true, value = "update projectassign set locked = 2 , lockedquestion = ?3, lockedtime = ?4 where projectid = ?1 and supplierid = ?2")
    void lockByProjectIdAndSupplierId(Integer projectId, Integer supplierId, Integer lockedQuestion, Timestamp lockedTime);

}
