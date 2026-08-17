package com.questionbook.dto;

import java.time.LocalDateTime;

public record ShareLinkResponse(Long id, String token, boolean isActive, LocalDateTime expiresAt) {}
