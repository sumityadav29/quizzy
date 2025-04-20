package com.acticwolf.quizzy.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class QuestionResponseDto {
    private Integer id;
    private String questionText;
    private List<String> options;
    private Integer correctOption;
    private Timestamp createdAt;
}