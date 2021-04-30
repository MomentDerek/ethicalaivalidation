package com.sydney.au.ethicalaivalidation.repository;

import com.sydney.au.ethicalaivalidation.domain.Segments;
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
public interface SegmentsRepository extends CrudRepository<Segments, Integer> {
    List<Segments> findByPrincipleidIn(List<Integer> principleId);

    List<Segments> findByPrincipleid(Integer principleId);

    List<Segments> findByIdIn(List<Integer> segmentId);

    Optional<Segments> findByPrincipleidAndSegmentname(Integer principleId, String segmentName);


    @Query(nativeQuery = true, value = "select distinct s.id,s.principleid,s.segmentname from (ethicalconcerns as e join questions q on q.id = e.questionid) join segments as s on q.segmentid = s.id where e.projectid = ?1")
    List<Segments> getSegmentsByProjectId(Integer projectId);

}
