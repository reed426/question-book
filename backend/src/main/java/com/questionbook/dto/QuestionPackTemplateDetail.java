package com.questionbook.dto;

import java.util.List;

public record QuestionPackTemplateDetail(Long id, String name, String targetType, List<TemplateQuestionResponse> questions) {}
