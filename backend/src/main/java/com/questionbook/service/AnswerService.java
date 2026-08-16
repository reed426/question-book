package com.questionbook.service;

import com.questionbook.dto.AnswerResponse;
import com.questionbook.entity.Answer;
import com.questionbook.entity.Question;
import com.questionbook.repository.AnswerRepository;
import com.questionbook.repository.QuestionRepository;
import com.questionbook.security.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;


import java.time.LocalDateTime;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class AnswerService {
    private final QuestionRepository questionRepository;
    private final AnswerRepository answerRepository;
    private final FileStorageService fileStorageService;

    public AnswerResponse saveAnswer(Long questionId, String content, MultipartFile image) {
        Question question = questionRepository.findById(questionId)
                .orElseThrow(() -> new NoSuchElementException("질문을 찾을 수 없습니다"));

        String ownerEmail = question.getQuestionSet().getUser().getEmail();
        if (!ownerEmail.equals(SecurityUtils.getCurrentUserEmail())) {
            throw new AccessDeniedException("본인의 질문에만 답변할 수 있습니다");
        }

        Answer answer = answerRepository.findByQuestionId(questionId).orElseGet(Answer::new);
        answer.setQuestion(question);
        answer.setContent(content);
        if (image != null && !image.isEmpty()) {
            answer.setImageUrl(fileStorageService.store(image));
        }
        answer.setUpdatedAt(LocalDateTime.now());
        Answer saved = answerRepository.save(answer);
        return new AnswerResponse(saved.getId(), saved.getContent(), saved.getImageUrl(), saved.getAnsweredAt(), saved.getUpdatedAt());
    }
    public AnswerResponse getAnswer(Long questionId) {
        Question question = questionRepository.findById(questionId)
                .orElseThrow(() -> new NoSuchElementException("질문을 찾을 수 없습니다"));
        String ownerEmail = question.getQuestionSet().getUser().getEmail();
        if (!ownerEmail.equals(SecurityUtils.getCurrentUserEmail())) {
            throw new AccessDeniedException("본인의 질문만 조회할 수 있습니다");
        }
        return answerRepository.findByQuestionId(questionId)
                .map(a -> new AnswerResponse(a.getId(), a.getContent(), a.getImageUrl(), a.getAnsweredAt(), a.getUpdatedAt()))
                .orElse(null);
    }
}
