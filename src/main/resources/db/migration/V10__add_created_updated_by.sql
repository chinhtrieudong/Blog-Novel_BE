-- Add created_by and updated_by columns to all tables that extend BaseEntity

-- Users table
ALTER TABLE users ADD COLUMN created_by BIGINT;
ALTER TABLE users ADD COLUMN updated_by BIGINT;

-- Posts table
ALTER TABLE posts ADD COLUMN created_by BIGINT;
ALTER TABLE posts ADD COLUMN updated_by BIGINT;

-- Novels table
ALTER TABLE novels ADD COLUMN created_by BIGINT;
ALTER TABLE novels ADD COLUMN updated_by BIGINT;

-- Chapters table
ALTER TABLE chapters ADD COLUMN created_by BIGINT;
ALTER TABLE chapters ADD COLUMN updated_by BIGINT;

-- Comments table
ALTER TABLE comments ADD COLUMN created_by BIGINT;
ALTER TABLE comments ADD COLUMN updated_by BIGINT;

-- Authors table
ALTER TABLE authors ADD COLUMN created_by BIGINT;
ALTER TABLE authors ADD COLUMN updated_by BIGINT;

-- Novel likes table
ALTER TABLE novel_likes ADD COLUMN created_by BIGINT;
ALTER TABLE novel_likes ADD COLUMN updated_by BIGINT;

-- Novel favorites table
ALTER TABLE novel_favorites ADD COLUMN created_by BIGINT;
ALTER TABLE novel_favorites ADD COLUMN updated_by BIGINT;

-- Novel ratings table
ALTER TABLE novel_ratings ADD COLUMN created_by BIGINT;
ALTER TABLE novel_ratings ADD COLUMN updated_by BIGINT;
