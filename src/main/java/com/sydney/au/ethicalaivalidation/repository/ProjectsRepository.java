package com.sydney.au.ethicalaivalidation.repository;

import com.sydney.au.ethicalaivalidation.domain.Projects;
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
public interface ProjectsRepository extends CrudRepository<Projects, Integer> {

    Projects findByProjectname(String projectName);

    List<Projects> findByCreatorid(Integer creatorId);

    List<Projects> findByStatus(Integer status);

    @Modifying
    @Query(nativeQuery = true, value = "update projects set status = ?2 where id = ?1")
    void updateStatusByProjectId(Integer projectId, Integer status);

}
