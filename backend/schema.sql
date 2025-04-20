-- 1. quizzes
CREATE TABLE quizzes (
    id SERIAL PRIMARY KEY,
    title VARCHAR NOT NULL,
    description TEXT,
    created_by VARCHAR,
    created_at TIMESTAMP DEFAULT now()
);

-- 2. questions
CREATE TABLE questions (
    id SERIAL PRIMARY KEY,
    quiz_id INTEGER NOT NULL REFERENCES quizzes(id) ON DELETE CASCADE,
    question_text TEXT NOT NULL,
    options_json JSONB NOT NULL CHECK (
        jsonb_typeof(options_json) = 'array'
    ),
    correct_option INTEGER NOT NULL,
    created_at TIMESTAMP DEFAULT now(),
    CONSTRAINT valid_correct_option CHECK (
        jsonb_array_length(options_json) > correct_option AND correct_option >= 0
    )
);

-- 3. game_sessions
CREATE TABLE game_sessions (
    id SERIAL PRIMARY KEY,
    quiz_id INTEGER NOT NULL REFERENCES quizzes(id) ON DELETE CASCADE,
    status VARCHAR NOT NULL CHECK (status IN ('WAITING', 'IN_PROGRESS', 'FINISHED')),
    current_question_id INTEGER REFERENCES questions(id),
    started_at TIMESTAMP,
    ended_at TIMESTAMP,
    room_code VARCHAR(6) UNIQUE NOT NULL
);

-- 4. players
CREATE TABLE players (
    id SERIAL PRIMARY KEY,
    session_id INTEGER NOT NULL REFERENCES game_sessions(id) ON DELETE CASCADE,
    nickname VARCHAR NOT NULL,
    joined_at TIMESTAMP DEFAULT now(),
    player_token VARCHAR(36) NOT NULL UNIQUE  -- You can still use a UUID string here if needed
);

-- 5. game_session_answers
CREATE TABLE game_session_answers (
    id SERIAL PRIMARY KEY,
    player_id INTEGER NOT NULL REFERENCES players(id) ON DELETE CASCADE,
    question_id INTEGER NOT NULL REFERENCES questions(id) ON DELETE CASCADE,
    selected_index INTEGER NOT NULL,
    submitted_at TIMESTAMP DEFAULT now(),
    is_correct BOOLEAN,
    response_time INTEGER,
    CONSTRAINT unique_answer_per_question UNIQUE (player_id, question_id)
);