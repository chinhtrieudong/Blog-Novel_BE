-- Update user roles to match new Role enum values (READER, AUTHOR, ADMIN)
-- First, alter the enum to include new values (MySQL allows this)
ALTER TABLE users MODIFY COLUMN role ENUM('USER','ADMIN','READER','AUTHOR') NOT NULL DEFAULT 'USER';

-- Then update USER roles to READER
UPDATE users SET role = 'READER' WHERE role = 'USER';

-- Optional: Update the default to READER for new users going forward
ALTER TABLE users MODIFY COLUMN role ENUM('USER','ADMIN','READER','AUTHOR') NOT NULL DEFAULT 'READER';
