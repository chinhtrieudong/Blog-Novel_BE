-- Add is_blocked column to comments table
ALTER TABLE comments ADD COLUMN is_blocked BOOLEAN DEFAULT FALSE;
