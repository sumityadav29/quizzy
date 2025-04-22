-- Insert Quizzes
INSERT INTO public.quizzes (title, description, created_by)
VALUES
  ('General Knowledge', 'A basic GK quiz covering various topics.', 'admin'),
  ('Science & Tech', 'A quiz focused on science, inventions, and tech.', 'admin'),
  ('Sports Trivia', 'Trivia questions from various sports.', 'admin');

-- Insert Questions for General Knowledge (quiz_id = 1)
INSERT INTO public.questions (quiz_id, question_text, options_json, correct_option)
VALUES
  (1, 'What is the capital of France?', '["London", "Paris", "Rome", "Berlin"]', 1),
  (1, 'Which planet is known as the Red Planet?', '["Earth", "Mars", "Jupiter", "Venus"]', 1),
  (1, 'How many continents are there?', '["5", "6", "7", "8"]', 2);

-- Insert Questions for Science & Tech (quiz_id = 2)
INSERT INTO public.questions (quiz_id, question_text, options_json, correct_option)
VALUES
  (2, 'Who developed the theory of relativity?', '["Isaac Newton", "Albert Einstein", "Galileo", "Tesla"]', 1),
  (2, 'What does CPU stand for?', '["Central Processing Unit", "Computer Personal Unit", "Core Power Unit", "Control Processing Unit"]', 0),
  (2, 'What is the boiling point of water?', '["90°C", "100°C", "110°C", "120°C"]', 1);

-- Insert Questions for Sports Trivia (quiz_id = 3)
INSERT INTO public.questions (quiz_id, question_text, options_json, correct_option)
VALUES
  (3, 'How many players are there in a football (soccer) team?', '["9", "10", "11", "12"]', 2),
  (3, 'Which country hosted the 2016 Olympics?', '["China", "Brazil", "UK", "Japan"]', 1),
  (3, 'Who has won the most Grand Slam titles in tennis?', '["Nadal", "Federer", "Djokovic", "Sampras"]', 2);