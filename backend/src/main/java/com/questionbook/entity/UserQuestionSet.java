package com.questionbook.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class UserQuestionSet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    @ManyToOne @JoinColumn(name = "template_id")
    private QuestionPackTemplate template;
    @Enumerated(EnumType.STRING)
    private QuestionMode mode;
    private Integer intervalDays;
    private LocalDate startDate;
    private LocalDateTime createdAt = LocalDateTime.now();
}
