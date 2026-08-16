package com.questionbook.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class ShareLink {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "question_set_id")
    private UserQuestionSet questionSet;
    @Column(unique = true, nullable = false)
    private String token;
    private boolean isActive = true;
    private LocalDateTime createdAt = LocalDateTime.now();
}
