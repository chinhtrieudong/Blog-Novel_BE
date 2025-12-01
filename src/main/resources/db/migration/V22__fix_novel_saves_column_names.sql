-- Add missing columns to novel_saves table with correct snake_case names

SET @db_name = DATABASE();

-- Add read_chapters if not exists
SET @sql = 'ALTER TABLE novel_saves ADD COLUMN read_chapters INT';
SET @check = (SELECT COUNT(*) FROM information_schema.COLUMNS WHERE TABLE_SCHEMA = @db_name AND TABLE_NAME = 'novel_saves' AND COLUMN_NAME = 'read_chapters');
SET @sql = IF(@check = 0, @sql, 'SELECT "read_chapters column already exists"');
PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

-- Add saved_at if not exists
SET @sql = 'ALTER TABLE novel_saves ADD COLUMN saved_at VARCHAR(255)';
SET @check = (SELECT COUNT(*) FROM information_schema.COLUMNS WHERE TABLE_SCHEMA = @db_name AND TABLE_NAME = 'novel_saves' AND COLUMN_NAME = 'saved_at');
SET @sql = IF(@check = 0, @sql, 'SELECT "saved_at column already exists"');
PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

-- Add last_read if not exists
SET @sql = 'ALTER TABLE novel_saves ADD COLUMN last_read VARCHAR(255)';
SET @check = (SELECT COUNT(*) FROM information_schema.COLUMNS WHERE TABLE_SCHEMA = @db_name AND TABLE_NAME = 'novel_saves' AND COLUMN_NAME = 'last_read');
SET @sql = IF(@check = 0, @sql, 'SELECT "last_read column already exists"');
PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;
