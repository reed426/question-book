package com.questionbook.dto;

import java.time.LocalDateTime;

public record CustomQuestionAdminView(
        Long questionId,
        String text,
        String authorNickname,
        String questionSetTitle,
        LocalDateTime createdAt
) {}
