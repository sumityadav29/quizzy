import React, { useEffect, useState } from 'react';
import { useParams } from 'react-router';
import { GameSessionControllerApi, LeaderboardEntryDto } from '../../api/quizzy';
import LeaderboardComponent from '../../components/LeaderboardComponent';

import './GameOverPage.css';

const GameOverPage: React.FC = () => {
  const { sessionId } = useParams();
  const [leaderboard, setLeaderboard] = useState<LeaderboardEntryDto[]>([]);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    if (!sessionId) return;

    const api = new GameSessionControllerApi();
    api.getLeaderboard(Number(sessionId))
      .then((res) => setLeaderboard(res.data))
      .catch((err) => {
        console.error(err);
        setError('Failed to fetch leaderboard');
      });
  }, [sessionId]);

  return (
    <div className="final-leaderboard-wrapper">
      <h2>🏁 Game Over</h2>
      {error ? (
        <p className="error-message">{error}</p>
      ) : (
        <LeaderboardComponent leaderboard={leaderboard} />
      )}
    </div>
  );
};

export default GameOverPage;