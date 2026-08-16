package com.questionbook.dto;

import com.questionbook.entity.QuestionMode;

import java.time.LocalDateTime;

public record QuestionSetSummary(
        Long id,
        String title,
        String targetType,
        QuestionMode mode,
        int total,
        int answered,
        LocalDateTime createdAt
) {
}
