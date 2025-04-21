package com.acticwolf.quizzy.dtos.quiz;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateQuizRequestDto {
    private String title;
    private String description;
    private String createdBy;
}