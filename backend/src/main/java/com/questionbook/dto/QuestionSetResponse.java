package com.questionbook.dto;

import com.questionbook.entity.QuestionMode;

import java.time.LocalDate;
import java.util.List;

public record QuestionSetResponse(
        Long id, QuestionMode mode, Integer intervalDays, LocalDate startDate, List<QuestionResponse> questions
) {}
