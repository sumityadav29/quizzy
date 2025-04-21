import React from 'react';
import { useNavigate } from 'react-router';

const LandingPage: React.FC = () => {
  const navigate = useNavigate();

  return (
    <div>
      <h1>Welcome to Quizzy!</h1>
      <p>Please choose:</p>
      <button onClick={() => navigate('/join')}>Join Existing Game</button>
      <button onClick={() => navigate('/host')}>Start New Game</button>
    </div>
  );
};

export default LandingPage;