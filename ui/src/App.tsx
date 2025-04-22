import React from 'react';
import { Routes, Route } from 'react-router';

import LandingPage from './pages/LandingPage';
import JoinExistingGamePage from './pages/gameplayer/JoinExistingGamePage';
import StartNewGamePage from './pages/gamehost/StartNewGamePage';
import GamePlayPage from './pages/gameplayer/GamePlayPage';
import CreateQuizPage from './pages/quizadmin/CreateQuizPage';
import AddQuestionsPage from './pages/quizadmin/AddQuestionsPage';
import { SessionProvider } from './contexts/SessionContext';

const App: React.FC = () => {
  return (
    <div className="App">
      <SessionProvider>
        <Routes>
          <Route path="/" element={<LandingPage />} />
          <Route path="/join" element={<JoinExistingGamePage />} />
          <Route path="/host" element={<StartNewGamePage />} />
          <Route path="/game" element={<GamePlayPage />} />
          <Route path="/admin/quiz/new" element={<CreateQuizPage />} />
          <Route path="/admin/quiz/:id/questions" element={<AddQuestionsPage />} />
        </Routes>
      </SessionProvider>
    </div>
  );
};

export default App;