import React from 'react';
import { useNavigate } from 'react-router';
import './LandingPage.css';

const LandingPage: React.FC = () => {
  const navigate = useNavigate();

  return (
    <div className="landing-container">
      <h1 className="landing-title">Welcome to Quizzy!</h1>
      <p className="landing-subtitle">Please choose:</p>
      <div className="landing-buttons">
        <button className="landing-button" onClick={() => navigate('/join')}>
          Join Existing Game
        </button>
        <button className="landing-button" onClick={() => navigate('/host')}>
          Start New Game
        </button>
      </div>
    </div>
  );
};

export default LandingPage;