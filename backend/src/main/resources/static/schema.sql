-- public.quizzes definition

-- Drop table

-- DROP TABLE public.quizzes;

CREATE TABLE public.quizzes (
	id serial4 NOT NULL,
	title varchar NOT NULL,
	description text NULL,
	created_by varchar NULL,
	created_at timestamp DEFAULT now() NULL,
	CONSTRAINT quizzes_pkey PRIMARY KEY (id)
);


-- public.questions definition

-- Drop table

-- DROP TABLE public.questions;

CREATE TABLE public.questions (
	id serial4 NOT NULL,
	quiz_id int4 NOT NULL,
	question_text text NOT NULL,
	options_json jsonb NOT NULL,
	correct_option int4 NOT NULL,
	created_at timestamp DEFAULT now() NULL,
	CONSTRAINT questions_options_json_check CHECK ((jsonb_typeof(options_json) = 'array'::text)),
	CONSTRAINT questions_pkey PRIMARY KEY (id),
	CONSTRAINT valid_correct_option CHECK (((jsonb_array_length(options_json) > correct_option) AND (correct_option >= 0))),
	CONSTRAINT questions_quiz_id_fkey FOREIGN KEY (quiz_id) REFERENCES public.quizzes(id) ON DELETE CASCADE
);


-- public.game_session_answers definition

-- Drop table

-- DROP TABLE public.game_session_answers;

CREATE TABLE public.game_session_answers (
	id serial4 NOT NULL,
	player_id int4 NOT NULL,
	question_id int4 NOT NULL,
	selected_index int4 NOT NULL,
	submitted_at timestamp DEFAULT now() NULL,
	is_correct bool NULL,
	response_time int4 NULL,
	score int4 DEFAULT 0 NULL,
	CONSTRAINT game_session_answers_pkey PRIMARY KEY (id),
	CONSTRAINT unique_answer_per_question UNIQUE (player_id, question_id)
);


-- public.game_sessions definition

-- Drop table

-- DROP TABLE public.game_sessions;

CREATE TABLE public.game_sessions (
	id serial4 NOT NULL,
	quiz_id int4 NOT NULL,
	status varchar NOT NULL,
	current_question_id int4 NULL,
	started_at timestamp NULL,
	ended_at timestamp NULL,
	room_code varchar(6) NOT NULL,
	created_at timestamp DEFAULT now() NOT NULL,
	winner serial4 NULL,
	round_time int4 DEFAULT 15 NULL,
	round_cooldown_time int4 DEFAULT 5 NULL,
	CONSTRAINT game_sessions_pkey PRIMARY KEY (id),
	CONSTRAINT game_sessions_room_code_key UNIQUE (room_code)
);


-- public.players definition

-- Drop table

-- DROP TABLE public.players;

CREATE TABLE public.players (
	id serial4 NOT NULL,
	session_id int4 NOT NULL,
	nickname varchar NOT NULL,
	joined_at timestamp DEFAULT now() NULL,
	player_token varchar(36) NOT NULL,
	score int4 DEFAULT 0 NULL,
	CONSTRAINT players_pkey PRIMARY KEY (id),
	CONSTRAINT players_player_token_key UNIQUE (player_token)
);


-- public.game_session_answers foreign keys

ALTER TABLE public.game_session_answers ADD CONSTRAINT game_session_answers_player_id_fkey FOREIGN KEY (player_id) REFERENCES public.players(id) ON DELETE CASCADE;
ALTER TABLE public.game_session_answers ADD CONSTRAINT game_session_answers_question_id_fkey FOREIGN KEY (question_id) REFERENCES public.questions(id) ON DELETE CASCADE;


-- public.game_sessions foreign keys

ALTER TABLE public.game_sessions ADD CONSTRAINT fk_game_sessions_winner FOREIGN KEY (winner) REFERENCES public.players(id);
ALTER TABLE public.game_sessions ADD CONSTRAINT game_sessions_current_question_id_fkey FOREIGN KEY (current_question_id) REFERENCES public.questions(id);
ALTER TABLE public.game_sessions ADD CONSTRAINT game_sessions_quiz_id_fkey FOREIGN KEY (quiz_id) REFERENCES public.quizzes(id) ON DELETE CASCADE;


-- public.players foreign keys

ALTER TABLE public.players ADD CONSTRAINT players_session_id_fkey FOREIGN KEY (session_id) REFERENCES public.game_sessions(id) ON DELETE CASCADE;