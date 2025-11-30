-- Create author_follows table
CREATE TABLE author_follows (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    author_id BIGINT NOT NULL,
    user_id BIGINT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    UNIQUE KEY uk_author_user_follow (author_id, user_id),
    CONSTRAINT fk_author_follow_author FOREIGN KEY (author_id) REFERENCES authors (id) ON DELETE CASCADE,
    CONSTRAINT fk_author_follow_user FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE
);
