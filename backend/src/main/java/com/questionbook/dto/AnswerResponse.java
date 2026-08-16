package com.questionbook.dto;

import java.time.LocalDateTime;

public record AnswerResponse(Long id, String content, String imageUrl, LocalDateTime answeredAt, LocalDateTime updatedAt) {}
