package com.questionbook.repository;

import com.questionbook.entity.UserQuestionSet;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserQuestionSetRepository extends JpaRepository<UserQuestionSet, Long> {}