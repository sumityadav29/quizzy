package com.acticwolf.quizzy.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LiveQuestionResponseDto {
    private Integer id;
    private String questionText;
    private List<String> options;
}
