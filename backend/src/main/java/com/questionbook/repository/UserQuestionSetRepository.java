package com.questionbook.repository;

import com.questionbook.entity.UserQuestionSet;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserQuestionSetRepository extends JpaRepository<UserQuestionSet, Long> {
    List<UserQuestionSet> findByUser_EmailOrderByCreatedAtDesc(String email);
    long countByUser_Email(String email);
}
