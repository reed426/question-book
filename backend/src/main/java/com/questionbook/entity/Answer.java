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
public class Answer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @OneToOne
    @JoinColumn(name = "question_id")
    private Question question;
    @Column(length = 2000)
    private String content;
    private String imageUrl;
    private LocalDateTime answeredAt = LocalDateTime.now();
    private LocalDateTime updatedAt;
}