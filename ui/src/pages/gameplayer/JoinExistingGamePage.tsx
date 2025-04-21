import React, { useState } from 'react';
import { useNavigate } from 'react-router';
import {
  GameSessionControllerApi,
  JoinGameSessionRequestDto,
  JoinGameSessionResponseDto,
} from '../../api/quizzy';
import './JoinExistingGamePage.css';

const JoinExistingGamePage: React.FC = () => {
  const [nickname, setNickname] = useState('');
  const [roomCode, setRoomCode] = useState('');
  const [error, setError] = useState<string | null>(null);
  const navigate = useNavigate();
  const api = new GameSessionControllerApi();

  const handleJoin = async (e: React.FormEvent) => {
    e.preventDefault();
    setError(null);

    try {
      const requestBody: JoinGameSessionRequestDto = { nickname: nickname.trim() };
      const res = await api.joinSessionByRoomCode(roomCode.trim().toUpperCase(), requestBody);
      const data: JoinGameSessionResponseDto = res.data;

      localStorage.setItem('playerToken', data.playerToken || '');
      localStorage.setItem('playerId', String(data.playerId));
      localStorage.setItem('nickname', data.nickname || '');
      localStorage.setItem('roomCode', roomCode.toUpperCase());

      navigate('/game');
    } catch (err) {
      setError('Failed to join game. Please check the room code.');
    }
  };

  return (
    <div className="join-container">
      <h2>Join Existing Game</h2>
      <form className="join-form" onSubmit={handleJoin}>
        <input
          type="text"
          placeholder="Nickname"
          value={nickname}
          onChange={(e) => setNickname(e.target.value)}
          required
        />
        <input
          type="text"
          placeholder="Room Code"
          value={roomCode}
          onChange={(e) => setRoomCode(e.target.value)}
          required
        />
        <button type="submit">Join Game</button>
      </form>
      {error && <p className="join-error">{error}</p>}
    </div>
  );
};

export default JoinExistingGamePage;