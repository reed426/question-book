package com.questionbook.service;

import com.questionbook.dto.AddQuestionRequest;
import com.questionbook.dto.QuestionResponse;
import com.questionbook.dto.UpdateQuestionRequest;
import com.questionbook.entity.Question;
import com.questionbook.entity.UserQuestionSet;
import com.questionbook.repository.QuestionRepository;
import com.questionbook.repository.UserQuestionSetRepository;
import com.questionbook.security.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class QuestionService {
    private final QuestionRepository questionRepository;
    private final UserQuestionSetRepository questionSetRepository;

    public QuestionResponse addQuestion(Long questionSetId, AddQuestionRequest req) {
        UserQuestionSet set = questionSetRepository.findById(questionSetId)
                .orElseThrow(() -> new NoSuchElementException("질문 세트를 찾을 수 없습니다"));
        checkOwner(set);

        int nextOrder = questionRepository.findByQuestionSetIdOrderBySortOrder(questionSetId).stream()
                .mapToInt(Question::getSortOrder).max().orElse(0) + 1;

        Question q = new Question();
        q.setQuestionSet(set);
        q.setSortOrder(nextOrder);
        q.setText(req.text());
        q.setCustom(true);
        Question saved = questionRepository.save(q);
        return new QuestionResponse(saved.getId(), saved.getSortOrder(), saved.getText(), saved.isCustom(), false, false);
    }

    public QuestionResponse updateQuestion(Long questionId, UpdateQuestionRequest req) {
        Question q = questionRepository.findById(questionId)
                .orElseThrow(() -> new NoSuchElementException("질문을 찾을 수 없습니다"));
        checkOwner(q.getQuestionSet());

        if (req.text() != null) q.setText(req.text());
        if (req.sortOrder() != null) q.setSortOrder(req.sortOrder());
        q.setCustom(true);
        Question saved = questionRepository.save(q);
        return new QuestionResponse(saved.getId(), saved.getSortOrder(), saved.getText(), saved.isCustom(), false, false);
    }

    public void deleteQuestion(Long questionId) {
        Question q = questionRepository.findById(questionId)
                .orElseThrow(() -> new NoSuchElementException("질문을 찾을 수 없습니다"));
        checkOwner(q.getQuestionSet());
        questionRepository.delete(q);
    }

    private void checkOwner(UserQuestionSet set) {
        if (!set.getUser().getEmail().equals(SecurityUtils.getCurrentUserEmail())) {
            throw new AccessDeniedException("본인의 질문 세트만 수정할 수 있습니다");
        }
    }
}