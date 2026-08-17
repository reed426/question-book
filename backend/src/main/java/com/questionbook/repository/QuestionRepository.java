package com.questionbook.repository;

import com.questionbook.entity.Question;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface QuestionRepository extends JpaRepository<Question, Long> {
    List<Question> findByQuestionSetIdOrderBySortOrder(Long id);
    List<Question> findTop50ByIsCustomTrueOrderByCreatedAtDesc();
    List<Question> findByQuestionSetIdIn(List<Long> questionSetIds);
}
