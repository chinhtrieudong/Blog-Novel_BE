-- Create authors table
CREATE TABLE authors
(
    id         BIGINT PRIMARY KEY AUTO_INCREMENT,
    name       VARCHAR(255) UNIQUE NOT NULL,
    bio        TEXT,
    avatar_url VARCHAR(255),
    user_id    BIGINT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT fk_author_user FOREIGN KEY (user_id) REFERENCES users (id)
);

-- Update novels table to make slug NOT NULL and update foreign key
ALTER TABLE novels
    MODIFY slug VARCHAR(255) NOT NULL,
    DROP CONSTRAINT fk_novel_author,
    ADD CONSTRAINT fk_novel_author FOREIGN KEY (author_id) REFERENCES authors (id);

-- Insert some default authors for existing novels
INSERT INTO authors (name, bio, created_at, updated_at)
SELECT DISTINCT
    COALESCE(u.full_name, u.username, 'Unknown Author') as name,
    u.bio,
    NOW(),
    NOW()
FROM novels n
         LEFT JOIN users u ON n.author_id = u.id;

-- Update novels to reference authors instead of users
UPDATE novels n
SET author_id = (
    SELECT a.id
    FROM authors a
    WHERE a.name = COALESCE(
        (SELECT u.full_name FROM users u WHERE u.id = n.author_id),
        (SELECT u.username FROM users u WHERE u.id = n.author_id),
        'Unknown Author'
    )
);

-- Generate slugs for existing novels that don't have them
UPDATE novels
SET slug = CONCAT(LOWER(REPLACE(REPLACE(REPLACE(title, ' ', '-'), '.', ''), ',', '')), '-', id)
WHERE slug IS NULL OR slug = '';

-- Make sure all novels have slugs
UPDATE novels
SET slug = CONCAT('novel-', id)
WHERE slug IS NULL OR slug = '';
