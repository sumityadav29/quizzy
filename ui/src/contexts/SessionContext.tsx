import React, { createContext, useContext, ReactNode, useState } from 'react';

interface SessionContextType {
  sessionId: string;
  playerToken: string;
  roomCode: string;
  nickname: string
  setSession: (session: Partial<Omit<SessionContextType, 'setSession'>>) => void;
}

const SessionContext = createContext<SessionContextType | undefined>(undefined);

export const useSession = () => {
  const context = useContext(SessionContext);
  if (!context) throw new Error('useSession must be used within a SessionProvider');
  return context;
};

interface Props {
  children: ReactNode;
}

export const SessionProvider: React.FC<Props> = ({ children }) => {
  const [sessionState, setSessionState] = useState<Omit<SessionContextType, 'setSession'>>({
    sessionId: '',
    playerToken: '',
    roomCode: '',
    nickname: ''
  });

  const setSession = (updates: Partial<Omit<SessionContextType, 'setSession'>>) => {
    setSessionState((prev) => ({ ...prev, ...updates }));
  };

  return (
    <SessionContext.Provider value={{ ...sessionState, setSession }}>
      {children}
    </SessionContext.Provider>
  );
};