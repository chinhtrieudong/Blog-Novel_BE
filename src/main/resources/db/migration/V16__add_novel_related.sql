-- Create novel_related table for manual related novels
CREATE TABLE novel_related (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    novel_id BIGINT NOT NULL,
    related_novel_id BIGINT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    UNIQUE KEY uk_novel_related (novel_id, related_novel_id),
    CONSTRAINT fk_novel_related_novel FOREIGN KEY (novel_id) REFERENCES novels (id) ON DELETE CASCADE,
    CONSTRAINT fk_novel_related_related FOREIGN KEY (related_novel_id) REFERENCES novels (id) ON DELETE CASCADE
);
