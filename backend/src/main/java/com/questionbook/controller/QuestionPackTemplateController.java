package com.questionbook.controller;

import com.questionbook.dto.QuestionPackTemplateDetail;
import com.questionbook.dto.QuestionPackTemplateSummary;
import com.questionbook.dto.TemplateQuestionResponse;
import com.questionbook.entity.QuestionPackTemplate;
import com.questionbook.entity.TemplateQuestion;
import com.questionbook.repository.QuestionPackTemplateRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Comparator;
import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/api/question-pack-templates")
@RequiredArgsConstructor
public class QuestionPackTemplateController {
    private final QuestionPackTemplateRepository repository;

    @GetMapping
    public List<QuestionPackTemplateSummary> list() {
        return repository.findAll().stream()
                .map(t -> new QuestionPackTemplateSummary(t.getId(), t.getName(), t.getTargetType()))
                .toList();
    }

    @GetMapping("/{id}")
    public QuestionPackTemplateDetail detail(@PathVariable Long id) {
        QuestionPackTemplate template = repository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("템플릿을 찾을 수 없습니다"));
        List<TemplateQuestionResponse> questions = template.getQuestions().stream()
                .sorted(Comparator.comparingInt(TemplateQuestion::getSortOrder))
                .map(q -> new TemplateQuestionResponse(q.getId(), q.getSortOrder(), q.getText()))
                .toList();
        return new QuestionPackTemplateDetail(template.getId(), template.getName(), template.getTargetType(), questions);
    }
}
