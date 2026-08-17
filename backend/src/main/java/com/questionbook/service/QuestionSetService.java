package com.questionbook.service;

import com.questionbook.dto.*;
import com.questionbook.entity.*;
import com.questionbook.exception.LimitExceededException;
import com.questionbook.repository.*;
import com.questionbook.security.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class QuestionSetService {
    private final UserRepository userRepository;
    private final QuestionPackTemplateRepository templateRepository;
    private final UserQuestionSetRepository questionSetRepository;
    private final QuestionRepository questionRepository;
    private final AnswerRepository answerRepository;

    private static final int MAX_QUESTION_SETS_PER_USER = 20;
    public QuestionSetResponse create(CreateQuestionSetRequest req) {
        User user = userRepository.findByEmail(SecurityUtils.getCurrentUserEmail())
                .orElseThrow(() -> new NoSuchElementException("사용자를 찾을 수 없습니다"));

        long currentCount = questionSetRepository.countByUser_Email(user.getEmail());
        if (currentCount >= MAX_QUESTION_SETS_PER_USER) {
            throw new LimitExceededException("질문북은 최대 " + MAX_QUESTION_SETS_PER_USER + "개까지 만들 수 있어요.");
        }

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
        UserQuestionSet set = getOwnedSet(id);
        List<Question> questions = questionRepository.findByQuestionSetIdOrderBySortOrder(id);
        return toResponse(set, questions);
    }

    public ProgressResponse getProgress(Long id) {
        UserQuestionSet set = getOwnedSet(id);
        List<Question> questions = questionRepository.findByQuestionSetIdOrderBySortOrder(id);
        int total = questions.size();
        int answeredCount = answerRepository.findAnsweredQuestionIds(id).size();
        int percentage = total == 0 ? 0 : (int) Math.round(answeredCount * 100.0 / total);
        return new ProgressResponse(total, answeredCount, percentage);
    }

    private UserQuestionSet getOwnedSet(Long id) {
        UserQuestionSet set = questionSetRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("질문 세트를 찾을 수 없습니다"));
        if (!set.getUser().getEmail().equals(SecurityUtils.getCurrentUserEmail())) {
            throw new AccessDeniedException("본인의 질문 세트만 조회할 수 있습니다");
        }
        return set;
    }

    private QuestionSetResponse toResponse(UserQuestionSet set, List<Question> questions) {
        Set<Long> answeredIds = new HashSet<>(answerRepository.findAnsweredQuestionIds(set.getId()));
        List<QuestionResponse> qrs = questions.stream()
                .map(q -> new QuestionResponse(
                        q.getId(), q.getSortOrder(), q.getText(), q.isCustom(),
                        isLocked(set, q), answeredIds.contains(q.getId())
                ))
                .toList();
        return new QuestionSetResponse(set.getId(), set.getMode(), set.getIntervalDays(), set.getStartDate(), qrs);
    }

    private boolean isLocked(UserQuestionSet set, Question q) {
        if (set.getMode() != QuestionMode.PERIODIC) return false;
        LocalDate unlockDate = set.getStartDate().plusDays((long) (q.getSortOrder() - 1) * set.getIntervalDays());
        return LocalDate.now().isBefore(unlockDate);
    }

    public BookPreviewResponse getPreview(Long id) {
        UserQuestionSet set = getOwnedSet(id);
        List<Answer> answers = answerRepository.findByQuestionSetIdOrderByQuestionSortOrder(id);
        List<BookEntryResponse> entries = answers.stream()
                .map(a -> new BookEntryResponse(
                        a.getQuestion().getId(), a.getQuestion().getSortOrder(), a.getQuestion().getText(),
                        a.getContent(), a.getImageUrl(), a.getAnsweredAt()
                ))
                .toList();
        return new BookPreviewResponse(set.getId(),set.getUser().getNickname(), entries.size(), entries);
    }

    public List<QuestionSetSummary> listMySets() {
        String email = SecurityUtils.getCurrentUserEmail();
        List<UserQuestionSet> sets = questionSetRepository.findByUser_EmailOrderByCreatedAtDesc(email);
        List<Long> setIds = sets.stream().map(UserQuestionSet::getId).toList();

        Map<Long, Long> totalBySet = questionRepository.findByQuestionSetIdIn(setIds).stream()
                .collect(Collectors.groupingBy(q -> q.getQuestionSet().getId(), Collectors.counting()));

        Map<Long, Long> answeredBySet = answerRepository.findByQuestionSetIds(setIds).stream()
                .collect(Collectors.groupingBy(a -> a.getQuestion().getQuestionSet().getId(), Collectors.counting()));

        return sets.stream().map(set -> {
            String title = set.getTemplate() != null ? set.getTemplate().getName() : "나만의 질문";
            String targetType = set.getTemplate() != null ? set.getTemplate().getTargetType() : null;
            int total = totalBySet.getOrDefault(set.getId(), 0L).intValue();
            int answered = answeredBySet.getOrDefault(set.getId(), 0L).intValue();
            return new QuestionSetSummary(set.getId(), title, targetType, set.getMode(), total, answered, set.getCreatedAt());
        }).toList();
    }
}
