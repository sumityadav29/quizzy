package com.acticwolf.quizzy.dtos.quiz;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QuizDetailResponseDto {
    private Integer id;
    private String title;
    private String description;
    private String createdBy;
    private Timestamp createdAt;
    private List<QuestionResponseDto> questions;
}