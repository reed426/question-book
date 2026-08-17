package com.questionbook.service;

import com.questionbook.dto.AdminStatsResponse;
import com.questionbook.dto.TemplateUsage;
import com.questionbook.entity.UserQuestionSet;
import com.questionbook.repository.AnswerRepository;
import com.questionbook.repository.QuestionRepository;
import com.questionbook.repository.UserQuestionSetRepository;
import com.questionbook.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminService {
    private final UserRepository userRepository;
    private final UserQuestionSetRepository questionSetRepository;
    private final QuestionRepository questionRepository;
    private final AnswerRepository answerRepository;

    public AdminStatsResponse getStats() {
        long totalUsers = userRepository.count();
        long totalQuestionSets = questionSetRepository.count();
        long totalQuestions = questionRepository.count();
        long totalAnswers = answerRepository.count();
        double averageCompletionRate = totalQuestions == 0 ? 0 : (totalAnswers * 100.0) / totalQuestions;

        List<UserQuestionSet> allSets = questionSetRepository.findAll();
        Map<String, Long> usage = allSets.stream()
                .collect(Collectors.groupingBy(
                        s -> s.getTemplate() != null ? s.getTemplate().getName() : "직접 만든 질문",
                        Collectors.counting()
                ));
        List<TemplateUsage> templateUsage = usage.entrySet().stream()
                .map(e -> new TemplateUsage(e.getKey(), e.getValue()))
                .toList();

        return new AdminStatsResponse(totalUsers, totalQuestionSets, totalAnswers, averageCompletionRate, templateUsage);
    }
}