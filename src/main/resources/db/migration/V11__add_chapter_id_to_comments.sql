-- Add chapter_id column to comments table for chapter-specific comments

ALTER TABLE comments ADD COLUMN chapter_id BIGINT;

ALTER TABLE comments ADD CONSTRAINT fk_comment_chapter
    FOREIGN KEY (chapter_id) REFERENCES chapters (id);
