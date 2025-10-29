-- Create novel_likes table
CREATE TABLE novel_likes (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    novel_id BIGINT NOT NULL,
    user_id BIGINT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    UNIQUE KEY uk_novel_user_like (novel_id, user_id),
    CONSTRAINT fk_novel_like_novel FOREIGN KEY (novel_id) REFERENCES novels (id) ON DELETE CASCADE,
    CONSTRAINT fk_novel_like_user FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE
);

-- Create novel_favorites table
CREATE TABLE novel_favorites (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    novel_id BIGINT NOT NULL,
    user_id BIGINT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    UNIQUE KEY uk_novel_user_favorite (novel_id, user_id),
    CONSTRAINT fk_novel_favorite_novel FOREIGN KEY (novel_id) REFERENCES novels (id) ON DELETE CASCADE,
    CONSTRAINT fk_novel_favorite_user FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE
);

-- Create novel_ratings table
CREATE TABLE novel_ratings (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    novel_id BIGINT NOT NULL,
    user_id BIGINT NOT NULL,
    rating INT NOT NULL CHECK (rating >= 1 AND rating <= 5),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    UNIQUE KEY uk_novel_user_rating (novel_id, user_id),
    CONSTRAINT fk_novel_rating_novel FOREIGN KEY (novel_id) REFERENCES novels (id) ON DELETE CASCADE,
    CONSTRAINT fk_novel_rating_user FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE
);
