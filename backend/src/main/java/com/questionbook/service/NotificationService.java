package com.questionbook.service;

import com.questionbook.dto.NotificationItem;
import com.questionbook.entity.Question;
import com.questionbook.entity.QuestionMode;
import com.questionbook.entity.UserQuestionSet;
import com.questionbook.repository.AnswerRepository;
import com.questionbook.repository.QuestionRepository;
import com.questionbook.repository.UserQuestionSetRepository;
import com.questionbook.security.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class NotificationService {
    private final UserQuestionSetRepository questionSetRepository;
    private final QuestionRepository questionRepository;
    private final AnswerRepository answerRepository;

    public List<NotificationItem> getNotifications() {
        String email = SecurityUtils.getCurrentUserEmail();
        List<UserQuestionSet> mySets = questionSetRepository.findByUser_EmailOrderByCreatedAtDesc(email);

        List<NotificationItem> result = new ArrayList<>();
        for (UserQuestionSet set : mySets) {
            String title = set.getTemplate() != null ? set.getTemplate().getName() : "나만의 질문";
            List<Question> questions = questionRepository.findByQuestionSetIdOrderBySortOrder(set.getId());
            Set<Long> answeredIds = new HashSet<>(answerRepository.findAnsweredQuestionIds(set.getId()));

            for (Question q : questions) {
                boolean locked = isLocked(set, q);
                if (!locked && !answeredIds.contains(q.getId())) {
                    result.add(new NotificationItem(set.getId(), title, q.getId(), q.getText()));
                }
            }
        }
        return result;
    }

    private boolean isLocked(UserQuestionSet set, Question q) {
        if (set.getMode() != QuestionMode.PERIODIC) return false;
        LocalDate unlockDate = set.getStartDate().plusDays((long) (q.getSortOrder() - 1) * set.getIntervalDays());
        return LocalDate.now().isBefore(unlockDate);
    }
}
