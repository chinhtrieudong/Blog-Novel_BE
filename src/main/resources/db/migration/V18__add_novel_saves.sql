-- Create novel_saves table
CREATE TABLE novel_saves (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    novel_id BIGINT NOT NULL,
    user_id BIGINT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    UNIQUE KEY uk_novel_user_save (novel_id, user_id),
    CONSTRAINT fk_novel_save_novel FOREIGN KEY (novel_id) REFERENCES novels (id) ON DELETE CASCADE,
    CONSTRAINT fk_novel_save_user FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE
);
