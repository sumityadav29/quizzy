import React, { useEffect, useState } from 'react';
import {
  GameSessionControllerApi,
  LiveQuestionResponseDto,
  SubmitAnswerRequestDto,
  SubmitAnswerResponseDto,
} from '../api/quizzy';

const GamePlayPage: React.FC = () => {
  const [question, setQuestion] = useState<LiveQuestionResponseDto | null>(null);
  const [selectedIndex, setSelectedIndex] = useState<number | null>(null);
  const [answered, setAnswered] = useState(false);
  const [response, setResponse] = useState<SubmitAnswerResponseDto | null>(null);
  const [error, setError] = useState<string | null>(null);

  const sessionId = localStorage.getItem('sessionId');
  const playerToken = localStorage.getItem('playerToken');
  const roomCode = localStorage.getItem('roomCode');

  const sessionApi = new GameSessionControllerApi();

  useEffect(() => {
    if (!roomCode || !playerToken) {
      setError('Missing player info');
      return;
    }

    const source = new EventSource(
      `http://localhost:8080/api/v1/events/sessions/code/${roomCode}/subscribe?playerToken=${encodeURIComponent(playerToken)}`
    );

    source.addEventListener('NEXT_QUESTION', (event) => {
      const data: LiveQuestionResponseDto = JSON.parse(event.data);
      setQuestion(data);
      setSelectedIndex(null);
      setAnswered(false);
      setResponse(null);
    });

    source.addEventListener('QUIZ_ENDED', () => {
      alert('Quiz ended!');
      window.location.href = `/leaderboard/${sessionId}`;
    });

    return () => {
      source.close();
    };
  }, [roomCode, playerToken, sessionId]);

  const handleAnswer = async (index: number) => {
    if (answered || selectedIndex !== null || !sessionId || !playerToken || !question?.id) return;

    setSelectedIndex(index);
    setAnswered(true);

    try {
      const req: SubmitAnswerRequestDto = {
        playerToken,
        selectedIndex: index,
      };

      const res = await sessionApi.submitAnswer(Number(sessionId), question.id, req);

      setResponse(res.data);
    } catch (err) {
      setError('Failed to submit answer.');
    }
  };

  return (
    <div>
      <h2>Quiz Game</h2>

      {error && <p style={{ color: 'red' }}>{error}</p>}

      {!question && <p>Waiting for question...</p>}

      {question && (
        <div>
          <h3>{question.questionText}</h3>
          <ul>
            {question.options?.map((opt, i) => (
              <li key={i}>
                <button
                  disabled={answered}
                  onClick={() => handleAnswer(i)}
                  style={{
                    background:
                      selectedIndex === i
                        ? response?.correct
                          ? 'lightgreen'
                          : 'salmon'
                        : undefined,
                  }}
                >
                  {opt}
                </button>
              </li>
            ))}
          </ul>

          {answered && response && (
            <p>
              {response.correct ? '✅ Correct!' : '❌ Incorrect.'} Response time:{' '}
              {response.responseTime}ms
            </p>
          )}
        </div>
      )}
    </div>
  );
};

export default GamePlayPage;