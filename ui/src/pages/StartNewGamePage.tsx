import React, { useEffect, useState } from 'react';
import { useNavigate } from 'react-router';
import {
  GameSessionControllerApi,
  QuizControllerApi,
  CreateGameSessionRequestDto,
  PaginatedQuizResponseDto,
} from '../api/quizzy';

const StartNewGamePage: React.FC = () => {
  const [paginatedQuizzes, setPaginatedQuizzes] = useState<PaginatedQuizResponseDto>({});
  const [selectedQuizId, setSelectedQuizId] = useState<number | null>(null);
  const [error, setError] = useState<string | null>(null);
  const navigate = useNavigate();

  const quizApi = new QuizControllerApi();
  const sessionApi = new GameSessionControllerApi();

  useEffect(() => {
    quizApi.getAllQuizzes().then((res) => setPaginatedQuizzes(res.data));
  }, []);

  const handleCreate = async (e: React.FormEvent) => {
    e.preventDefault();
    setError(null);

    if (!selectedQuizId) {
      setError('Please select a quiz to host.');
      return;
    }

    try {
      const req: CreateGameSessionRequestDto = { quizId: selectedQuizId };
      const res = await sessionApi.createSession(req);

      localStorage.setItem('isHost', 'true');
      localStorage.setItem('roomCode', res.data.roomCode || '');
      localStorage.setItem('sessionId', String(res.data.id));

      localStorage.setItem('isHost', 'true');

      navigate('/game');
    } catch (err) {
      setError('Failed to create game session.');
    }
  };

  return (
    <div>
      <h2>Start a New Game</h2>
      <form onSubmit={handleCreate}>
        <select
          value={selectedQuizId ?? ''}
          onChange={(e) => setSelectedQuizId(Number(e.target.value))}
          required
        >
          <option value="">-- Select Quiz --</option>
          {paginatedQuizzes.quizzes?.map((quiz) => (
            <option key={quiz.id} value={quiz.id}>
              {quiz.title}
            </option>
          ))}
        </select>
        <button type="submit">Start Game</button>
      </form>
      {error && <p>{error}</p>}
    </div>
  );
};

export default StartNewGamePage;