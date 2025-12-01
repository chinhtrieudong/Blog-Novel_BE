-- Fix column naming in novel_saves table to match JPA expectations
-- Rename columns from camelCase to snake_case

-- Rename lastRead to last_read
ALTER TABLE novel_saves CHANGE COLUMN lastRead last_read VARCHAR(255);

-- Rename readChapters to read_chapters
ALTER TABLE novel_saves CHANGE COLUMN readChapters read_chapters INT;

-- Rename savedAt to saved_at
ALTER TABLE novel_saves CHANGE COLUMN savedAt saved_at VARCHAR(255);
