package com.questionbook.dto;

import java.util.List;

public record AdminStatsResponse(
        long totalUsers,
        long totalQuestionSets,
        long totalAnswers,
        double averageCompletionRate,
        List<TemplateUsage> templateUsage
) {}