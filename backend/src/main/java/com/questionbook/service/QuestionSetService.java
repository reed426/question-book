package com.questionbook.service;

import com.questionbook.dto.CreateQuestionSetRequest;
import com.questionbook.dto.QuestionResponse;
import com.questionbook.dto.QuestionSetResponse;
import com.questionbook.entity.*;
import com.questionbook.repository.QuestionPackTemplateRepository;
import com.questionbook.repository.QuestionRepository;
import com.questionbook.repository.UserQuestionSetRepository;
import com.questionbook.repository.UserRepository;
import com.questionbook.security.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class QuestionSetService {
    private final UserRepository userRepository;
    private final QuestionPackTemplateRepository templateRepository;
    private final UserQuestionSetRepository questionSetRepository;
    private final QuestionRepository questionRepository;

    public QuestionSetResponse create(CreateQuestionSetRequest req) {
        User user = userRepository.findByEmail(SecurityUtils.getCurrentUserEmail())
                .orElseThrow(() -> new NoSuchElementException("사용자를 찾을 수 없습니다"));

        UserQuestionSet set = new UserQuestionSet();
        set.setUser(user);
        set.setMode(req.mode());
        set.setIntervalDays(req.mode() == QuestionMode.PERIODIC ? req.intervalDays() : null);
        set.setStartDate(req.startDate() != null ? req.startDate() : LocalDate.now());

        if (req.templateId() != null) {
            QuestionPackTemplate template = templateRepository.findById(req.templateId())
                    .orElseThrow(() -> new NoSuchElementException("템플릿을 찾을 수 없습니다"));
            set.setTemplate(template);
        }

        UserQuestionSet saved = questionSetRepository.save(set);

        List<Question> questions = new ArrayList<>();
        if (set.getTemplate() != null) {
            for (TemplateQuestion tq : set.getTemplate().getQuestions()) {
                Question q = new Question();
                q.setQuestionSet(saved);
                q.setSortOrder(tq.getSortOrder());
                q.setText(tq.getText());
                q.setCustom(false);
                questions.add(q);
            }
            questionRepository.saveAll(questions);
        }

        return toResponse(saved, questions);
    }

    public QuestionSetResponse get(Long id) {
        UserQuestionSet set = questionSetRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("질문 세트를 찾을 수 없습니다"));
        if (!set.getUser().getEmail().equals(SecurityUtils.getCurrentUserEmail())) {
            throw new AccessDeniedException("본인의 질문 세트만 조회할 수 있습니다");
        }
        List<Question> questions = questionRepository.findByQuestionSetIdOrderBySortOrder(id);
        return toResponse(set, questions);
    }

    private QuestionSetResponse toResponse(UserQuestionSet set, List<Question> questions) {
        List<QuestionResponse> qrs = questions.stream()
                .map(q -> new QuestionResponse(q.getId(), q.getSortOrder(), q.getText(), q.isCustom(), isLocked(set, q)))
                .toList();
        return new QuestionSetResponse(set.getId(), set.getMode(), set.getIntervalDays(), set.getStartDate(), qrs);
    }

    private boolean isLocked(UserQuestionSet set, Question q) {
        if (set.getMode() != QuestionMode.PERIODIC) return false;
        LocalDate unlockDate = set.getStartDate().plusDays((long) (q.getSortOrder()-1) * set.getIntervalDays());
        return LocalDate.now().isBefore(unlockDate);
    }
}
