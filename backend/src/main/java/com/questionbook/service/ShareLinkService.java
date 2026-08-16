package com.questionbook.service;

import com.questionbook.dto.BookEntryResponse;
import com.questionbook.dto.BookPreviewResponse;
import com.questionbook.dto.ShareLinkResponse;
import com.questionbook.entity.Answer;
import com.questionbook.entity.ShareLink;
import com.questionbook.entity.UserQuestionSet;
import com.questionbook.repository.AnswerRepository;
import com.questionbook.repository.ShareLinkRepository;
import com.questionbook.repository.UserQuestionSetRepository;
import com.questionbook.security.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ShareLinkService {
    private final UserQuestionSetRepository questionSetRepository;
    private final ShareLinkRepository shareLinkRepository;
    private final AnswerRepository answerRepository;

    public ShareLinkResponse create(Long questionSetId) {
        UserQuestionSet set = questionSetRepository.findById(questionSetId)
                .orElseThrow(() -> new NoSuchElementException("질문 세트를 찾을 수 없습니다"));
        if (!set.getUser().getEmail().equals(SecurityUtils.getCurrentUserEmail())) {
            throw new AccessDeniedException("본인의 질문 세트만 공유할 수 있습니다");
        }

        ShareLink link = new ShareLink();
        link.setQuestionSet(set);
        link.setToken(UUID.randomUUID().toString());
        link.setActive(true);
        ShareLink saved = shareLinkRepository.save(link);
        return new ShareLinkResponse(saved.getId(), saved.getToken(), saved.isActive());
    }

    public void deactivate(Long shareLinkId) {
        ShareLink link = shareLinkRepository.findById(shareLinkId)
                .orElseThrow(() -> new NoSuchElementException("공유 링크를 찾을 수 없습니다"));
        if (!link.getQuestionSet().getUser().getEmail().equals(SecurityUtils.getCurrentUserEmail())) {
            throw new AccessDeniedException("본인의 공유 링크만 해제할 수 있습니다");
        }
        link.setActive(false);
        shareLinkRepository.save(link);
    }

    public BookPreviewResponse getSharedPreview(String token) {
        ShareLink link = shareLinkRepository.findByToken(token)
                .filter(ShareLink::isActive)
                .orElseThrow(() -> new NoSuchElementException("유효하지 않거나 해제된 공유 링크입니다"));

        Long questionSetId = link.getQuestionSet().getId();
        List<Answer> answers = answerRepository.findByQuestionSetIdOrderByQuestionSortOrder(questionSetId);
        List<BookEntryResponse> entries = answers.stream()
                .map(a -> new BookEntryResponse(
                        a.getQuestion().getId(), a.getQuestion().getSortOrder(), a.getQuestion().getText(),
                        a.getContent(), a.getImageUrl(), a.getAnsweredAt()
                ))
                .toList();
        return new BookPreviewResponse(questionSetId, entries.size(), entries);
    }
}
