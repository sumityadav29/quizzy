import React from 'react';
import './LeaderboardPage.css';
import { LeaderboardEntryDto } from '../api/quizzy';


interface LeaderboardPageProps {
  leaderboard: LeaderboardEntryDto[];
}

const LeaderboardPage: React.FC<LeaderboardPageProps> = ({ leaderboard }) => {
  return (
    <div className="leaderboard-container">
      <h2>🏆 Leaderboard</h2>
      <ul className="leaderboard-list">
        {leaderboard
          .sort((a, b) => (b.score || 0) - (a.score || 0))
          .map((entry, index) => (
            <li key={index} className="leaderboard-item">
              <span>{index + 1}. {entry.nickname}</span>
              <span>{entry.score}</span>
            </li>
        ))}
      </ul>
    </div>
  );
};

export default LeaderboardPage;
