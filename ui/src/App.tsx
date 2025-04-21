import React from 'react';
import { Routes, Route } from 'react-router';

import LandingPage from './pages/LandingPage';
import JoinExistingGamePage from './pages/JoinExistingGamePage';
import StartNewGamePage from './pages/StartNewGamePage';
import GamePlayPage from './pages/GamePlayPage';

const App: React.FC = () => {
  return (
    <div className="App">
      <Routes>
        <Route path="/" element={<LandingPage />} />
        <Route path="/join" element={<JoinExistingGamePage />} />
        <Route path="/host" element={<StartNewGamePage />} />
        <Route path="/game" element={<GamePlayPage />} />
      </Routes>
    </div>
  );
};

export default App;