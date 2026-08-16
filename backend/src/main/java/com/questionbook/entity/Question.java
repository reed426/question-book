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
public class Question {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "question_set_id")
    private UserQuestionSet questionSet;
    private int sortOrder;
    @Column(length = 500)
    private String text;
    private boolean isCustom;
    private LocalDateTime createdAt = LocalDateTime.now();
}
