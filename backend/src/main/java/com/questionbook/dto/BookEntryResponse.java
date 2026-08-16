package com.questionbook.dto;

import java.time.LocalDateTime;

public record BookEntryResponse(
        Long questionId, int sortOrder, String questionText, String content, String imageUrl, LocalDateTime answeredAt
) {}
