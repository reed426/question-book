package com.questionbook.dto;

import java.util.List;

public record BookPreviewResponse(Long questionSetId, int totalAnswered, List<BookEntryResponse> entries) {}