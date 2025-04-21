package com.acticwolf.quizzy.dtos.quiz;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaginatedQuizResponseDto {
    private List<QuizListResponseDto> quizzes;
    private int page;
    private int size;
    private long totalElements;
    private int totalPages;
    private boolean last;
}