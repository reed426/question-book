package com.questionbook.dto;

import com.questionbook.entity.QuestionMode;

import java.time.LocalDate;

public record CreateQuestionSetRequest(Long templateId, QuestionMode mode, Integer intervalDays, LocalDate startDate) {}

