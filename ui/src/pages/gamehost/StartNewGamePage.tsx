import React, { useEffect, useState } from 'react';
import { useNavigate } from 'react-router';
import {
  GameSessionControllerApi,
  QuizControllerApi,
  CreateGameSessionRequestDto,
  PaginatedQuizResponseDto,
} from '../../api/quizzy';
import './StartNewGamePage.css';

const StartNewGamePage: React.FC = () => {
  const [paginatedQuizzes, setPaginatedQuizzes] = useState<PaginatedQuizResponseDto>({});
  const [selectedQuizId, setSelectedQuizId] = useState<number | null>(null);
  const [roundTime, setRoundTime] = useState<number>(15);
  const [cooldownTime, setCooldownTime] = useState<number>(5);
  const [error, setError] = useState<string | null>(null);
  const [roomCode, setRoomCode] = useState<string | null>(null);
  const [sessionId, setSessionId] = useState<number | null>(null);
  const [gameStarted, setGameStarted] = useState<boolean>(false);


  const quizApi = new QuizControllerApi();
  const sessionApi = new GameSessionControllerApi();

  useEffect(() => {
    quizApi.getAllQuizzes().then((res) => setPaginatedQuizzes(res.data));
  }, []);

  const handleCreateRoom = async (e: React.FormEvent) => {
    e.preventDefault();
    setError(null);
    setRoomCode(null);

    if (!selectedQuizId) {
      setError('Please select a quiz to host.');
      return;
    }

    try {
      const req: CreateGameSessionRequestDto = {
        quizId: selectedQuizId,
        roundTime: roundTime,
        roundCooldownTime: cooldownTime,
      };
      const res = await sessionApi.createSession(req);

      setRoomCode(res.data.roomCode || '');
      setSessionId(res.data.id || null);
      localStorage.setItem('roomCode', res.data.roomCode || '');
      localStorage.setItem('sessionId', String(res.data.id));
    } catch (err) {
      setError('Failed to create game session.');
    }
  };

  const handleStartGame = async () => {
    if (!sessionId) {
      setError('Session not found.');
      return;
    }

    try {
      await sessionApi.startSession(sessionId);
      setGameStarted(true);
    } catch (err) {
      setError('Failed to start the game.');
    }
  };

  return (
    <div className="start-game-container">
      <h2>Start a New Game</h2>
      <form className="start-game-form" onSubmit={handleCreateRoom}>
        <div className="form-group">
          <label htmlFor="quizSelect">Select Quiz </label>
          <select
            id="quizSelect"
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
        </div>

        <div className="form-group">
          <label htmlFor="roundTime">Round Time (in seconds) </label>
          <input
            type="number"
            id="roundTime"
            min="10"
            step="5"
            value={roundTime}
            onChange={(e) => setRoundTime(Number(e.target.value))}
            className="input-field"
          />
        </div>

        <div className="form-group">
          <label htmlFor="cooldownTime">Cooldown Time (in seconds) </label>
          <input
            type="number"
            id="cooldownTime"
            min="5"
            step="5"
            value={cooldownTime}
            onChange={(e) => setCooldownTime(Number(e.target.value))}
            className="input-field"
          />
        </div>

        <button type="submit" className="submit-btn">
          Create Room
        </button>
      </form>

      {error && <p className="start-game-error">{error}</p>}

      {roomCode && (
        <div className="room-code-container">
          <p>Your room code: <strong>{roomCode}</strong></p>
          <button onClick={handleStartGame} className="start-game-btn" disabled={gameStarted}>
            {gameStarted ? 'Game Started' : 'Start Game'}
          </button>
        </div>
      )}
    </div>
  );
};

export default StartNewGamePage;