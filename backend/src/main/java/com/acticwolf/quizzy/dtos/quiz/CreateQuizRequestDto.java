package com.acticwolf.quizzy.dtos.quiz;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateQuizRequestDto {
    private String title;
    private String description;
    private String createdBy;
}