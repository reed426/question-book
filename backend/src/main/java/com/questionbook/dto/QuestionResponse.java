package com.questionbook.dto;

public record QuestionResponse(Long id, int sortOrder, String text, boolean isCustom, boolean locked, boolean answered) {}

