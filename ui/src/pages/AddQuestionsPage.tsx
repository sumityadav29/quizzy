import React, { useEffect, useState } from 'react';
import { useParams } from 'react-router';
import {
  QuizControllerApi,
  AddQuestionRequestDto,
  QuizDetailResponseDto,
  QuestionResponseDto,
} from '../api/quizzy';

import './AddQuestionsPage.css';

const AddQuestionsPage: React.FC = () => {
  const { id: quizIdParam } = useParams();
  const quizId = Number(quizIdParam);

  const [quiz, setQuiz] = useState<QuizDetailResponseDto | null>(null);
  const [questionText, setQuestionText] = useState('');
  const [options, setOptions] = useState(['', '', '', '']);
  const [correctIndex, setCorrectIndex] = useState<number | null>(null);
  const [submittedQuestions, setSubmittedQuestions] = useState<QuestionResponseDto[]>([]);
  const [error, setError] = useState<string | null>(null);

  const quizApi = new QuizControllerApi();

  useEffect(() => {
    if (!quizId) return;
    quizApi.getQuiz(quizId).then((res) => setQuiz(res.data));
  }, [quizId]);

  const handleChangeOption = (index: number, value: string) => {
    const updated = [...options];
    updated[index] = value;
    setOptions(updated);
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setError(null);

    if (correctIndex === null || options.some((opt) => !opt.trim())) {
      setError('All options must be filled and a correct answer selected.');
      return;
    }

    const req: AddQuestionRequestDto = {
      questionText: questionText.trim(),
      options,
      correctOption: correctIndex,
    };

    try {
      const res = await quizApi.addQuestion(quizId, req);

      setSubmittedQuestions([...submittedQuestions, res.data]);
      setQuestionText('');
      setOptions(['', '', '', '']);
      setCorrectIndex(null);
    } catch (err) {
      setError('Failed to add question.');
    }
  };

  return (
    <div className="add-question-container">
      <h2>Add Questions to Quiz: {quiz?.title}</h2>
  
      <form className="add-question-form" onSubmit={handleSubmit}>
        <div>
          <label>Question Text:</label>
          <textarea
            required
            value={questionText}
            onChange={(e) => setQuestionText(e.target.value)}
          />
        </div>
  
        <div>
          <label>Options:</label>
          {options.map((opt, i) => (
            <div key={i} className="option-row">
              <input
                type="text"
                value={opt}
                onChange={(e) => handleChangeOption(i, e.target.value)}
                required
              />
              <label>
                <input
                  type="radio"
                  name="correct"
                  checked={correctIndex === i}
                  onChange={() => setCorrectIndex(i)}
                />{' '}
              </label>
            </div>
          ))}
        </div>
  
        <button type="submit">Add Question</button>
        {error && <p className="question-error">{error}</p>}
      </form>
  
      {/* Optional submitted questions display */}
      {/* <div className="submitted-questions">
        <h3>Submitted Questions</h3>
        <ul>
          {submittedQuestions.map((q, index) => (
            <li key={index}>
              <strong>{q.questionText}</strong> (Correct: {q.options?.[q.correctOption || 0]})
            </li>
          ))}
        </ul>
      </div> */}
    </div>
  );
};

export default AddQuestionsPage;