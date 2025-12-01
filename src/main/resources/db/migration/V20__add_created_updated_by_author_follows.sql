-- Add created_by and updated_by columns to tables that extend BaseEntity but were created after V10

-- Author follows table
ALTER TABLE author_follows ADD COLUMN created_by BIGINT;
ALTER TABLE author_follows ADD COLUMN updated_by BIGINT;

-- Novel related table
ALTER TABLE novel_related ADD COLUMN created_by BIGINT;
ALTER TABLE novel_related ADD COLUMN updated_by BIGINT;

-- Post saves table
ALTER TABLE post_saves ADD COLUMN created_by BIGINT;
ALTER TABLE post_saves ADD COLUMN updated_by BIGINT;

-- Novel saves table
ALTER TABLE novel_saves ADD COLUMN created_by BIGINT;
ALTER TABLE novel_saves ADD COLUMN updated_by BIGINT;
