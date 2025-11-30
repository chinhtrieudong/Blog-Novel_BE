-- Add user-specific fields to novel_saves table
ALTER TABLE novel_saves
ADD COLUMN readChapters INT,
ADD COLUMN savedAt VARCHAR(255),
ADD COLUMN lastRead VARCHAR(255);
