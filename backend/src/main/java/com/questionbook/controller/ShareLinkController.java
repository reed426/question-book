package com.questionbook.controller;

import com.questionbook.dto.BookPreviewResponse;
import com.questionbook.dto.ShareLinkResponse;
import com.questionbook.service.ShareLinkService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class ShareLinkController {
    private final ShareLinkService shareLinkService;

    @PostMapping("/api/question-sets/{id}/share-links")
    public ShareLinkResponse create(@PathVariable Long id) {
        return shareLinkService.create(id);
    }

    @DeleteMapping("/api/share-links/{id}")
    public ResponseEntity<Void> deactivate(@PathVariable Long id) {
        shareLinkService.deactivate(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/api/share/{token}")
    public BookPreviewResponse getShared(@PathVariable String token) {
        return shareLinkService.getSharedPreview(token);
    }
}
