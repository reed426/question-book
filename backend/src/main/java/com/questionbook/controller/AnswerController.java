package com.questionbook.controller;

import com.questionbook.dto.AnswerResponse;
import com.questionbook.service.AnswerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/questions/{questionId}/answer")
@RequiredArgsConstructor
public class AnswerController {
    private final AnswerService answerService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public AnswerResponse saveAnswer(
            @PathVariable Long questionId,
            @RequestParam String content,
            @RequestParam(required = false) MultipartFile image
    ) {
        return answerService.saveAnswer(questionId, content, image);
    }
    @GetMapping
    public AnswerResponse get(@PathVariable Long questionId) {
        return answerService.getAnswer(questionId);
    }
}
