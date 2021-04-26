package com.sydney.au.ethicalaivalidation.repository;

import com.sydney.au.ethicalaivalidation.domain.Segments;
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
public interface SegmentsRepository extends CrudRepository<Segments, Integer> {
    List<Segments> findByPrincipleidIn(List<Integer> principleId);

    List<Segments> findByIdIn(List<Integer> segmentId);
}
