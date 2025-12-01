-- Add created_by and updated_by columns to tables that extend BaseEntity but were created after V10

-- Author follows table
SET @db_name = DATABASE();
SET @table_name = 'author_follows';

-- Add created_by if not exists
SET @sql = CONCAT('ALTER TABLE ', @table_name, ' ADD COLUMN created_by BIGINT');
SET @check = (SELECT COUNT(*) FROM information_schema.COLUMNS WHERE TABLE_SCHEMA = @db_name AND TABLE_NAME = @table_name AND COLUMN_NAME = 'created_by');
SET @sql = IF(@check = 0, @sql, 'SELECT "created_by column already exists"');
PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

-- Add updated_by if not exists
SET @sql = CONCAT('ALTER TABLE ', @table_name, ' ADD COLUMN updated_by BIGINT');
SET @check = (SELECT COUNT(*) FROM information_schema.COLUMNS WHERE TABLE_SCHEMA = @db_name AND TABLE_NAME = @table_name AND COLUMN_NAME = 'updated_by');
SET @sql = IF(@check = 0, @sql, 'SELECT "updated_by column already exists"');
PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

-- Novel related table
SET @table_name = 'novel_related';

-- Add created_by if not exists
SET @sql = CONCAT('ALTER TABLE ', @table_name, ' ADD COLUMN created_by BIGINT');
SET @check = (SELECT COUNT(*) FROM information_schema.COLUMNS WHERE TABLE_SCHEMA = @db_name AND TABLE_NAME = @table_name AND COLUMN_NAME = 'created_by');
SET @sql = IF(@check = 0, @sql, 'SELECT "created_by column already exists"');
PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

-- Add updated_by if not exists
SET @sql = CONCAT('ALTER TABLE ', @table_name, ' ADD COLUMN updated_by BIGINT');
SET @check = (SELECT COUNT(*) FROM information_schema.COLUMNS WHERE TABLE_SCHEMA = @db_name AND TABLE_NAME = @table_name AND COLUMN_NAME = 'updated_by');
SET @sql = IF(@check = 0, @sql, 'SELECT "updated_by column already exists"');
PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

-- Post saves table
SET @table_name = 'post_saves';

-- Add created_by if not exists
SET @sql = CONCAT('ALTER TABLE ', @table_name, ' ADD COLUMN created_by BIGINT');
SET @check = (SELECT COUNT(*) FROM information_schema.COLUMNS WHERE TABLE_SCHEMA = @db_name AND TABLE_NAME = @table_name AND COLUMN_NAME = 'created_by');
SET @sql = IF(@check = 0, @sql, 'SELECT "created_by column already exists"');
PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

-- Add updated_by if not exists
SET @sql = CONCAT('ALTER TABLE ', @table_name, ' ADD COLUMN updated_by BIGINT');
SET @check = (SELECT COUNT(*) FROM information_schema.COLUMNS WHERE TABLE_SCHEMA = @db_name AND TABLE_NAME = @table_name AND COLUMN_NAME = 'updated_by');
SET @sql = IF(@check = 0, @sql, 'SELECT "updated_by column already exists"');
PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

-- Novel saves table
SET @table_name = 'novel_saves';

-- Add created_by if not exists
SET @sql = CONCAT('ALTER TABLE ', @table_name, ' ADD COLUMN created_by BIGINT');
SET @check = (SELECT COUNT(*) FROM information_schema.COLUMNS WHERE TABLE_SCHEMA = @db_name AND TABLE_NAME = @table_name AND COLUMN_NAME = 'created_by');
SET @sql = IF(@check = 0, @sql, 'SELECT "created_by column already exists"');
PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

-- Add updated_by if not exists
SET @sql = CONCAT('ALTER TABLE ', @table_name, ' ADD COLUMN updated_by BIGINT');
SET @check = (SELECT COUNT(*) FROM information_schema.COLUMNS WHERE TABLE_SCHEMA = @db_name AND TABLE_NAME = @table_name AND COLUMN_NAME = 'updated_by');
SET @sql = IF(@check = 0, @sql, 'SELECT "updated_by column already exists"');
PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;
