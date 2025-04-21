package com.acticwolf.quizzy.dtos.quiz;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QuizListResponseDto {
    private Integer id;
    private String title;
    private String description;
    private String createdBy;
    private Timestamp createdAt;
}
