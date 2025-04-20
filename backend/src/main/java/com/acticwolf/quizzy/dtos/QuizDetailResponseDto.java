package com.acticwolf.quizzy.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class QuizDetailResponseDto {
    private Integer id;
    private String title;
    private String description;
    private String createdBy;
    private Timestamp createdAt;
}