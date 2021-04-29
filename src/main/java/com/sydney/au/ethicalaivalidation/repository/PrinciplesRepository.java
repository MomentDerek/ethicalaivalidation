package com.sydney.au.ethicalaivalidation.repository;

import com.sydney.au.ethicalaivalidation.domain.Principles;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.security.core.parameters.P;
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
public interface PrinciplesRepository extends CrudRepository<Principles, Integer> {

    @Modifying
    @Query(nativeQuery = true, value = "select distinct p.id,p.principlename from ((ethicalconcerns as e join questions q on q.id = e.questionid) join segments as s on q.segmentid = s.id) join principles as p on p.id = s.principleid where e.projectid = ?1")
    List<Principles> getPrincipleByProjectId(Integer projectId);

    List<Principles> findByIdIn(List<Integer> principleId);

    Optional<Principles> findByPrinciplename(String principleName);
}
