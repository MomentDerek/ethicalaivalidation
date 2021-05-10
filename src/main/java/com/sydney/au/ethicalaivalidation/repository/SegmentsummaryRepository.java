package com.sydney.au.ethicalaivalidation.repository;

import com.sydney.au.ethicalaivalidation.domain.Segmentsummary;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.Collection;
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
public interface SegmentsummaryRepository extends CrudRepository<Segmentsummary, Integer> {
    Optional<Segmentsummary> findByProjectidAndSegmentidAndValidatorid(Integer projectId, Integer segmentId, Integer validatorId);

    List<Segmentsummary> findByProjectidAndValidatoridAndSegmentidIn(Integer projectId, Integer validatorId, List<Integer> segmentId);

    @Modifying
    @Query(nativeQuery = true, value = "update segmentsummary set summary = ?4 ,createdtime = ?5 where projectid = ?1 and validatorid = ?2 and segmentid = ?3")
    void updateByProjectIdAndValidatorIdAndSubquesid(Integer projectId, Integer validatorId, Integer segmentId, String comment, Timestamp createTime);


    Collection<Segmentsummary> findByProjectid(int id);
}
