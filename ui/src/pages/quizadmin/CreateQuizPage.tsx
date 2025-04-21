import React, { useState } from 'react';
import { useNavigate } from 'react-router';
import {
  QuizControllerApi,
  CreateQuizRequestDto,
  QuizDetailResponseDto,
} from '../../api/quizzy';

import './CreateQuizPage.css';

const CreateQuizPage: React.FC = () => {
  const [title, setTitle] = useState('');
  const [description, setDescription] = useState('');
  const [createdBy, setCreatedBy] = useState('');
  const [error, setError] = useState<string | null>(null);
  const [createdQuiz, setCreatedQuiz] = useState<QuizDetailResponseDto | null>(null);

  const navigate = useNavigate();
  const quizApi = new QuizControllerApi();

  const handleCreate = async (e: React.FormEvent) => {
    e.preventDefault();
    setError(null);

    const req: CreateQuizRequestDto = {
      title: title.trim(),
      description: description.trim(),
      createdBy: createdBy.trim() || 'admin',
    };

    try {
      const res = await quizApi.createQuiz(req);
      setCreatedQuiz(res.data);
    } catch (err) {
      console.error(err);
      setError('Failed to create quiz.');
    }
  };

  const handleAddQuestions = () => {
    if (createdQuiz?.id) {
      navigate(`/admin/quiz/${createdQuiz.id}/questions`);
    }
  };

  return (
    <div className="create-quiz-container">
      <h2>Create a New Quiz</h2>
      <form className="create-quiz-form" onSubmit={handleCreate}>
        <div>
          <label>Title:</label>
          <input
            type="text"
            required
            value={title}
            onChange={(e) => setTitle(e.target.value)}
          />
        </div>
  
        <div>
          <label>Description:</label>
          <textarea
            required
            value={description}
            onChange={(e) => setDescription(e.target.value)}
          />
        </div>
  
        <div>
          <label>Created By:</label>
          <input
            type="text"
            value={createdBy}
            onChange={(e) => setCreatedBy(e.target.value)}
            placeholder="admin"
          />
        </div>
  
        <button type="submit">Create Quiz</button>
        {error && <p className="quiz-error">{error}</p>}
      </form>
  
      {createdQuiz && (
        <div className="quiz-success">
          <p>✅ Quiz “{createdQuiz.title}” created successfully.</p>
          <button onClick={handleAddQuestions}>➕ Add Questions</button>
        </div>
      )}
    </div>
  );
};

export default CreateQuizPage;