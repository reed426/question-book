package com.questionbook.controller;

import com.questionbook.dto.CreateQuestionSetRequest;
import com.questionbook.dto.QuestionSetResponse;
import com.questionbook.service.QuestionSetService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/question-sets")
@RequiredArgsConstructor
public class QuestionSetController {
    private final QuestionSetService questionSetService;

    @PostMapping
    public QuestionSetResponse create(@RequestBody CreateQuestionSetRequest req) {
        return questionSetService.create(req);
    }

    @GetMapping("/{id}")
    public QuestionSetResponse get(@PathVariable Long id) {
        return questionSetService.get(id);
    }
}