package com.questionbook.controller;

import com.questionbook.dto.AddQuestionRequest;
import com.questionbook.dto.QuestionResponse;
import com.questionbook.dto.UpdateQuestionRequest;
import com.questionbook.service.QuestionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class QuestionController {
    private final QuestionService questionService;

    @PostMapping("/api/question-sets/{questionSetId}/questions")
    public QuestionResponse add(@PathVariable Long questionSetId, @RequestBody AddQuestionRequest req) {
        return questionService.addQuestion(questionSetId, req);
    }

    @PatchMapping("/api/questions/{questionId}")
    public QuestionResponse update(@PathVariable Long questionId, @RequestBody UpdateQuestionRequest req) {
        return questionService.updateQuestion(questionId, req);
    }

    @DeleteMapping("/api/questions/{questionId}")
    public ResponseEntity<Void> delete(@PathVariable Long questionId) {
        questionService.deleteQuestion(questionId);
        return ResponseEntity.noContent().build();
    }
}
