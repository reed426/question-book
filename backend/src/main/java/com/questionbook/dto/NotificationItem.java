package com.questionbook.dto;

public record NotificationItem(Long questionSetId, String questionSetTitle, Long questionId, String questionText) {
}
