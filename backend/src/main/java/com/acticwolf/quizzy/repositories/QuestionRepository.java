package com.acticwolf.quizzy.repositories;

import com.acticwolf.quizzy.models.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuestionRepository extends JpaRepository<Question, Integer> {
    List<Question> findByQuizId(Integer quizId);
}