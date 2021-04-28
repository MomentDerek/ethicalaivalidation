package com.sydney.au.ethicalaivalidation.repository;

import com.sydney.au.ethicalaivalidation.domain.Questions;
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
public interface QuestionsRepository extends CrudRepository<Questions, Integer> {

    List<Questions> findBySegmentidIn(List<Integer> segmenId);

    List<Questions> findBySegmentid(Integer segmenId);

    List<Questions> findByIdIn(List<Integer> questionId);

    Optional<Questions> findBySegmentidAndAndQuestioncontent(Integer segmentId, String questionContent);

}
