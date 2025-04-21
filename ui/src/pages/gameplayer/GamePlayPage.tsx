import React, { useEffect, useState } from 'react';
import {
  GameSessionControllerApi,
  LeaderboardEntryDto,
  LiveQuestionResponseDto,
  SubmitAnswerRequestDto,
  SubmitAnswerResponseDto,
} from '../../api/quizzy';

import LeaderboardPage from '../../components/LeaderboardPage';

import './GamePlayPage.css';

const GamePlayPage: React.FC = () => {
  const [question, setQuestion] = useState<LiveQuestionResponseDto | null>(null);
  const [selectedIndex, setSelectedIndex] = useState<number | null>(null);
  const [answered, setAnswered] = useState(false);
  const [response, setResponse] = useState<SubmitAnswerResponseDto | null>(null);
  const [error, setError] = useState<string | null>(null);
  const [timeRemaining, setTimeRemaining] = useState<number>(0);
  const [canSubmit, setCanSubmit] = useState<boolean>(true);

  const [showLeaderboard, setShowLeaderboard] = useState(false);
  const [leaderboard, setLeaderboard] = useState<LeaderboardEntryDto[]>([]);

  const sessionId = localStorage.getItem('sessionId');
  const playerToken = localStorage.getItem('playerToken');
  const roomCode = localStorage.getItem('roomCode');

  // const isHost = localStorage.getItem('isHost') === 'true';

  const sessionApi = new GameSessionControllerApi();

  useEffect(() => {
    if (!roomCode || !playerToken) {
      setError('Missing player info');
      return;
    }

    const source = new EventSource(
      `http://localhost:8080/api/v1/events/sessions/code/${roomCode}/subscribe?playerToken=${encodeURIComponent(playerToken)}`
    );

    source.addEventListener('ROUND_TIME_UP', async () => {
      try {
        const res = await sessionApi.getLeaderboard(Number(sessionId));
        setLeaderboard(res.data);
        setShowLeaderboard(true);
      } catch (e) {
        console.error("Failed to fetch leaderboard");
      }
    });

    source.addEventListener('NEXT_QUESTION', (event) => {
      const data: LiveQuestionResponseDto = JSON.parse(event.data);
      setShowLeaderboard(false);
      setQuestion(data);
      setSelectedIndex(null);
      setAnswered(false);
      setResponse(null);

      setTimeRemaining(data.maximumAllowedTime || 15);
      setCanSubmit(true);

      const timer = setInterval(() => {
        setTimeRemaining((prev) => {
          if (prev <= 1) {
            clearInterval(timer);
            setCanSubmit(false);
            return 0;
          }
          return prev - 1;
        });
      }, 1000);
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
    if (answered || selectedIndex !== null || !sessionId || !playerToken || !question?.id || !canSubmit) return;

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

  const handleStartQuiz = async () => {
    try {
      if (!sessionId) return;
      await sessionApi.startSession(Number(sessionId));
      console.log('Quiz started');
      await handleNextQuestion();
    } catch (err) {
      console.error('Failed to start quiz', err);
    }
  };
  
  const handleNextQuestion = async () => {
    try {
      if (!sessionId) return;
      await sessionApi.sendNextQuestion(Number(sessionId));
      console.log('Sent next question');
    } catch (err) {
      console.error('Failed to send next question', err);
    }
  };
  
  const handleEndQuiz = async () => {
    try {
      alert('Ending quiz');
    } catch (err) {
      console.error('Failed to end quiz', err);
    }
  };

  return (
    <div className="gameplay-container">
      <h2>Quiz Game</h2>
  
      {error && <p style={{ color: 'red' }}>{error}</p>}
  
      {!question && <p>Waiting for question...</p>}
  
      {showLeaderboard ? (<LeaderboardPage leaderboard={leaderboard} />) 
      : question && (
        <>
          <h3>{question.questionText}</h3>
          <ul className="options-list">
            {question.options?.map((opt, i) => (
              <li key={i}>
                <button
                  className={`option-button ${
                    selectedIndex === i && response
                      ? response.correct
                        ? 'correct'
                        : 'incorrect'
                      : ''
                  }`}
                  disabled={answered || !canSubmit}
                  onClick={() => handleAnswer(i)}
                >
                  {opt}
                </button>
              </li>
            ))}
          </ul>
  
          {answered && response && (
            <p className="result-text">
              {response.correct ? '✅ Correct!' : '❌ Incorrect.'} Response time: {response.responseTime}ms
            </p>
          )}

          <div className="timer">
            <p>Time Remaining: {timeRemaining} seconds</p>
          </div>
  
          {/* {isHost && (
            <div className="host-controls">
              {!question && (
                <button onClick={handleStartQuiz}>Start Quiz</button>
              )}
              <button onClick={handleNextQuestion}>Next Question</button>
              <button onClick={handleEndQuiz}>End Quiz</button>
            </div>
          )} */}
        </>
      )}
    </div>
  );
};

export default GamePlayPage;