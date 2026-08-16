package com.questionbook.repository;

import com.questionbook.entity.Answer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface AnswerRepository extends JpaRepository<Answer, Long> {
    Optional<Answer> findByQuestionId(Long questionId);

    @Query("select a.question.id from Answer a where a.question.questionSet.id = :questionSetId")
    List<Long> findAnsweredQuestionIds(@Param("questionSetId") Long questionSetId);

    @Query("select a from Answer a where a.question.questionSet.id = :questionSetId order by a.question.sortOrder")
    List<Answer> findByQuestionSetIdOrderByQuestionSortOrder(@Param("questionSetId") Long questionSetId);
}
