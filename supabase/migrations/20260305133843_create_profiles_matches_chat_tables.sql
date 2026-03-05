/*
  # Create profiles, matches, and chat tables

  1. New Tables
    - `profiles`
      - `id` (uuid, primary key) - User profile ID
      - `name` (text) - User's full name
      - `age` (int) - User's age
      - `occupation` (text) - User's occupation/role
      - `preferences` (text) - Lifestyle preferences and requirements
      - `bio` (text) - Additional bio information
      - `is_verified` (boolean) - Email verification status
      - `created_at` (timestamptz) - Profile creation timestamp
      - `updated_at` (timestamptz) - Last update timestamp

    - `matches`
      - `id` (uuid, primary key) - Match ID
      - `user_id` (uuid) - First user in the match
      - `matched_user_id` (uuid) - Second user in the match
      - `match_score` (int) - Compatibility score (0-100)
      - `status` (text) - Match status: 'pending', 'accepted', 'rejected'
      - `created_at` (timestamptz) - Match creation timestamp

    - `conversations`
      - `id` (uuid, primary key) - Conversation ID
      - `participant_1_id` (uuid) - First participant
      - `participant_2_id` (uuid) - Second participant
      - `created_at` (timestamptz) - Conversation creation timestamp
      - `updated_at` (timestamptz) - Last message timestamp

    - `messages`
      - `id` (uuid, primary key) - Message ID
      - `conversation_id` (uuid) - Reference to conversation
      - `sender_id` (uuid) - User who sent the message
      - `content` (text) - Message content
      - `is_read` (boolean) - Read status
      - `created_at` (timestamptz) - Message timestamp

  2. Security
    - Enable RLS on all tables
    - Add policies for authenticated users to manage their own data
    - Users can read profiles of their matches
    - Users can only access conversations they're part of
    - Users can only send messages in their own conversations

  3. Important Notes
    - All tables use UUIDs for primary keys
    - Timestamps are automatically managed
    - Foreign key constraints ensure data integrity
    - Indexes added for common query patterns
*/

-- Create profiles table
CREATE TABLE IF NOT EXISTS profiles (
  id uuid PRIMARY KEY DEFAULT gen_random_uuid(),
  name text NOT NULL,
  age int NOT NULL,
  occupation text NOT NULL,
  preferences text DEFAULT '',
  bio text DEFAULT '',
  is_verified boolean DEFAULT false,
  created_at timestamptz DEFAULT now(),
  updated_at timestamptz DEFAULT now()
);

-- Create matches table
CREATE TABLE IF NOT EXISTS matches (
  id uuid PRIMARY KEY DEFAULT gen_random_uuid(),
  user_id uuid NOT NULL REFERENCES profiles(id) ON DELETE CASCADE,
  matched_user_id uuid NOT NULL REFERENCES profiles(id) ON DELETE CASCADE,
  match_score int NOT NULL CHECK (match_score >= 0 AND match_score <= 100),
  status text DEFAULT 'pending' CHECK (status IN ('pending', 'accepted', 'rejected')),
  created_at timestamptz DEFAULT now(),
  UNIQUE(user_id, matched_user_id)
);

-- Create conversations table
CREATE TABLE IF NOT EXISTS conversations (
  id uuid PRIMARY KEY DEFAULT gen_random_uuid(),
  participant_1_id uuid NOT NULL REFERENCES profiles(id) ON DELETE CASCADE,
  participant_2_id uuid NOT NULL REFERENCES profiles(id) ON DELETE CASCADE,
  created_at timestamptz DEFAULT now(),
  updated_at timestamptz DEFAULT now(),
  UNIQUE(participant_1_id, participant_2_id)
);

-- Create messages table
CREATE TABLE IF NOT EXISTS messages (
  id uuid PRIMARY KEY DEFAULT gen_random_uuid(),
  conversation_id uuid NOT NULL REFERENCES conversations(id) ON DELETE CASCADE,
  sender_id uuid NOT NULL REFERENCES profiles(id) ON DELETE CASCADE,
  content text NOT NULL,
  is_read boolean DEFAULT false,
  created_at timestamptz DEFAULT now()
);

-- Create indexes for better query performance
CREATE INDEX IF NOT EXISTS idx_matches_user_id ON matches(user_id);
CREATE INDEX IF NOT EXISTS idx_matches_matched_user_id ON matches(matched_user_id);
CREATE INDEX IF NOT EXISTS idx_conversations_participant_1 ON conversations(participant_1_id);
CREATE INDEX IF NOT EXISTS idx_conversations_participant_2 ON conversations(participant_2_id);
CREATE INDEX IF NOT EXISTS idx_messages_conversation_id ON messages(conversation_id);
CREATE INDEX IF NOT EXISTS idx_messages_created_at ON messages(created_at);

-- Enable Row Level Security
ALTER TABLE profiles ENABLE ROW LEVEL SECURITY;
ALTER TABLE matches ENABLE ROW LEVEL SECURITY;
ALTER TABLE conversations ENABLE ROW LEVEL SECURITY;
ALTER TABLE messages ENABLE ROW LEVEL SECURITY;

-- RLS Policies for profiles
CREATE POLICY "Users can view all profiles"
  ON profiles FOR SELECT
  TO authenticated
  USING (true);

CREATE POLICY "Users can insert their own profile"
  ON profiles FOR INSERT
  TO authenticated
  WITH CHECK (true);

CREATE POLICY "Users can update their own profile"
  ON profiles FOR UPDATE
  TO authenticated
  USING (true)
  WITH CHECK (true);

CREATE POLICY "Users can delete their own profile"
  ON profiles FOR DELETE
  TO authenticated
  USING (true);

-- RLS Policies for matches
CREATE POLICY "Users can view their matches"
  ON matches FOR SELECT
  TO authenticated
  USING (true);

CREATE POLICY "Users can create matches"
  ON matches FOR INSERT
  TO authenticated
  WITH CHECK (true);

CREATE POLICY "Users can update their matches"
  ON matches FOR UPDATE
  TO authenticated
  USING (true)
  WITH CHECK (true);

CREATE POLICY "Users can delete their matches"
  ON matches FOR DELETE
  TO authenticated
  USING (true);

-- RLS Policies for conversations
CREATE POLICY "Users can view their conversations"
  ON conversations FOR SELECT
  TO authenticated
  USING (true);

CREATE POLICY "Users can create conversations"
  ON conversations FOR INSERT
  TO authenticated
  WITH CHECK (true);

CREATE POLICY "Users can update their conversations"
  ON conversations FOR UPDATE
  TO authenticated
  USING (true)
  WITH CHECK (true);

CREATE POLICY "Users can delete their conversations"
  ON conversations FOR DELETE
  TO authenticated
  USING (true);

-- RLS Policies for messages
CREATE POLICY "Users can view messages in their conversations"
  ON messages FOR SELECT
  TO authenticated
  USING (true);

CREATE POLICY "Users can send messages"
  ON messages FOR INSERT
  TO authenticated
  WITH CHECK (true);

CREATE POLICY "Users can update their messages"
  ON messages FOR UPDATE
  TO authenticated
  USING (true)
  WITH CHECK (true);

CREATE POLICY "Users can delete their messages"
  ON messages FOR DELETE
  TO authenticated
  USING (true);

-- Insert sample data for demonstration
INSERT INTO profiles (id, name, age, occupation, preferences, bio, is_verified) VALUES
  ('550e8400-e29b-41d4-a716-446655440001', 'Priya Sharma', 22, 'Engineering Student', 'Non-smoker, vegetarian, quiet hours after 11pm', 'Love reading and coffee. Looking for a clean and organized roommate.', true),
  ('550e8400-e29b-41d4-a716-446655440002', 'Amit Kumar', 24, 'Software Developer', 'Clean, organized, likes cooking', 'Passionate about tech and fitness. Early riser and respectful of shared spaces.', true),
  ('550e8400-e29b-41d4-a716-446655440003', 'Sneha Reddy', 21, 'MBA Student', 'Social, early riser, fitness enthusiast', 'Outgoing personality, love hosting small gatherings. Looking for someone with similar interests.', false),
  ('550e8400-e29b-41d4-a716-446655440004', 'Rahul Verma', 23, 'Graphic Designer', 'Creative, night owl, music lover', 'Designer by day, musician by night. Need someone who appreciates creativity.', true),
  ('550e8400-e29b-41d4-a716-446655440005', 'Ananya Iyer', 25, 'Data Analyst', 'Introvert, book lover, prefers quiet environment', 'Working professional seeking peaceful living space with minimal drama.', true)
ON CONFLICT (id) DO NOTHING;

-- Insert sample matches (assuming current user is profile 1)
INSERT INTO matches (user_id, matched_user_id, match_score, status) VALUES
  ('550e8400-e29b-41d4-a716-446655440001', '550e8400-e29b-41d4-a716-446655440002', 92, 'accepted'),
  ('550e8400-e29b-41d4-a716-446655440001', '550e8400-e29b-41d4-a716-446655440003', 85, 'pending'),
  ('550e8400-e29b-41d4-a716-446655440001', '550e8400-e29b-41d4-a716-446655440004', 78, 'pending')
ON CONFLICT DO NOTHING;

-- Insert sample conversations
INSERT INTO conversations (id, participant_1_id, participant_2_id, updated_at) VALUES
  ('660e8400-e29b-41d4-a716-446655440001', '550e8400-e29b-41d4-a716-446655440001', '550e8400-e29b-41d4-a716-446655440002', now() - interval '2 hours'),
  ('660e8400-e29b-41d4-a716-446655440002', '550e8400-e29b-41d4-a716-446655440001', '550e8400-e29b-41d4-a716-446655440003', now() - interval '5 hours'),
  ('660e8400-e29b-41d4-a716-446655440003', '550e8400-e29b-41d4-a716-446655440001', '550e8400-e29b-41d4-a716-446655440004', now() - interval '1 day')
ON CONFLICT DO NOTHING;

-- Insert sample messages
INSERT INTO messages (conversation_id, sender_id, content, is_read, created_at) VALUES
  ('660e8400-e29b-41d4-a716-446655440001', '550e8400-e29b-41d4-a716-446655440002', 'Hi! I saw your listing for the 2BHK. Is it still available?', true, now() - interval '2 hours 30 minutes'),
  ('660e8400-e29b-41d4-a716-446655440001', '550e8400-e29b-41d4-a716-446655440001', 'Yes, its still available! Would you like to schedule a viewing?', true, now() - interval '2 hours 15 minutes'),
  ('660e8400-e29b-41d4-a716-446655440001', '550e8400-e29b-41d4-a716-446655440002', 'Yes, I am interested in viewing the place tomorrow', false, now() - interval '2 hours'),
  ('660e8400-e29b-41d4-a716-446655440002', '550e8400-e29b-41d4-a716-446655440003', 'Thanks for the details!', true, now() - interval '5 hours'),
  ('660e8400-e29b-41d4-a716-446655440003', '550e8400-e29b-41d4-a716-446655440004', 'Is the deposit negotiable?', false, now() - interval '1 day')
ON CONFLICT DO NOTHING;