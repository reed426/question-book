package com.questionbook.controller;

import com.questionbook.dto.*;
import com.questionbook.service.QuestionSetService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/question-sets")
@RequiredArgsConstructor
public class QuestionSetController {
    private final QuestionSetService questionSetService;

    @GetMapping
    public List<QuestionSetSummary> list() {
        return questionSetService.listMySets();
    }

    @PostMapping
    public QuestionSetResponse create(@RequestBody CreateQuestionSetRequest req) {
        return questionSetService.create(req);
    }

    @GetMapping("/{id}")
    public QuestionSetResponse get(@PathVariable Long id) {
        return questionSetService.get(id);
    }
    @GetMapping("/{id}/progress")
    public ProgressResponse progress(@PathVariable Long id) {
        return questionSetService.getProgress(id);
    }

    @GetMapping("/{id}/preview")
    public BookPreviewResponse preview(@PathVariable Long id) {
        return questionSetService.getPreview(id);
    }

}