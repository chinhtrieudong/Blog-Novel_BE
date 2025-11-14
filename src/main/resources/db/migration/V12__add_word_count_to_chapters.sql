-- Add word_count column to chapters table
ALTER TABLE chapters ADD COLUMN word_count BIGINT DEFAULT 0;
