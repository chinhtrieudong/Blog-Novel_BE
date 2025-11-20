-- USERS
CREATE TABLE users
(
    id         BIGINT PRIMARY KEY AUTO_INCREMENT,
    username   VARCHAR(50) UNIQUE  NOT NULL,
    email      VARCHAR(100) UNIQUE NOT NULL,
    password   VARCHAR(255)        NOT NULL,
    full_name  VARCHAR(100),
    bio        TEXT,
    avatar_url VARCHAR(255),
    role       ENUM ('READER','AUTHOR','ADMIN')    DEFAULT 'READER',
    status     ENUM ('ACTIVE','BANNED') DEFAULT 'ACTIVE',
    created_at TIMESTAMP                DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP                DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- POSTS
CREATE TABLE posts
(
    id          BIGINT PRIMARY KEY AUTO_INCREMENT,
    title       VARCHAR(255)        NOT NULL,
    slug        VARCHAR(255) UNIQUE NOT NULL,
    content     LONGTEXT,
    cover_image VARCHAR(255),
    author_id   BIGINT              NOT NULL,
    status      ENUM ('DRAFT','PUBLISHED','ARCHIVED','PENDING_REVIEW') DEFAULT 'DRAFT',
    view_count  BIGINT                                DEFAULT 0,
    created_at  TIMESTAMP                             DEFAULT CURRENT_TIMESTAMP,
    updated_at  TIMESTAMP                             DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT fk_post_author FOREIGN KEY (author_id) REFERENCES users (id)
);

-- CATEGORIES
CREATE TABLE categories
(
    id   BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100) UNIQUE NOT NULL,
    slug VARCHAR(100) UNIQUE NOT NULL
);

-- TAGS
CREATE TABLE tags
(
    id   BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100) UNIQUE NOT NULL,
    slug VARCHAR(100) UNIQUE NOT NULL
);

-- POST_CATEGORIES (N:N)
CREATE TABLE post_categories
(
    post_id     BIGINT NOT NULL,
    category_id BIGINT NOT NULL,
    PRIMARY KEY (post_id, category_id),
    CONSTRAINT fk_pc_post FOREIGN KEY (post_id) REFERENCES posts (id),
    CONSTRAINT fk_pc_category FOREIGN KEY (category_id) REFERENCES categories (id)
);

-- POST_TAGS (N:N)
CREATE TABLE post_tags
(
    post_id BIGINT NOT NULL,
    tag_id  BIGINT NOT NULL,
    PRIMARY KEY (post_id, tag_id),
    CONSTRAINT fk_pt_post FOREIGN KEY (post_id) REFERENCES posts (id),
    CONSTRAINT fk_pt_tag FOREIGN KEY (tag_id) REFERENCES tags (id)
);

-- NOVELS
CREATE TABLE novels
(
    id          BIGINT PRIMARY KEY AUTO_INCREMENT,
    title       VARCHAR(255)        NOT NULL,
    slug        VARCHAR(255) UNIQUE NOT NULL,
    description TEXT,
    cover_image VARCHAR(255),
    author_id   BIGINT              NOT NULL,
    status      ENUM ('ONGOING','COMPLETED','DROPPED') DEFAULT 'ONGOING',
    view_count  BIGINT                                 DEFAULT 0,
    avg_rating  FLOAT                                  DEFAULT 0,
    created_at  TIMESTAMP                              DEFAULT CURRENT_TIMESTAMP,
    updated_at  TIMESTAMP                              DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT fk_novel_author FOREIGN KEY (author_id) REFERENCES users (id)
);

-- GENRES
CREATE TABLE genres
(
    id   BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100) UNIQUE NOT NULL,
    slug VARCHAR(100) UNIQUE NOT NULL
);

-- NOVEL_GENRES (N:N)
CREATE TABLE novel_genres
(
    novel_id BIGINT NOT NULL,
    genre_id BIGINT NOT NULL,
    PRIMARY KEY (novel_id, genre_id),
    CONSTRAINT fk_ng_novel FOREIGN KEY (novel_id) REFERENCES novels (id),
    CONSTRAINT fk_ng_genre FOREIGN KEY (genre_id) REFERENCES genres (id)
);

-- CHAPTERS
CREATE TABLE chapters
(
    id             BIGINT PRIMARY KEY AUTO_INCREMENT,
    novel_id       BIGINT NOT NULL,
    title          VARCHAR(255),
    content        LONGTEXT,
    chapter_number INT,
    view_count     BIGINT    DEFAULT 0,
    created_at     TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at     TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT fk_chapter_novel FOREIGN KEY (novel_id) REFERENCES novels (id)
);

-- COMMENTS (cho Post hoặc Novel)
CREATE TABLE comments
(
    id         BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id    BIGINT NOT NULL,
    post_id    BIGINT,
    novel_id   BIGINT,
    content    TEXT   NOT NULL,
    parent_id  BIGINT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT fk_comment_user FOREIGN KEY (user_id) REFERENCES users (id),
    CONSTRAINT fk_comment_post FOREIGN KEY (post_id) REFERENCES posts (id),
    CONSTRAINT fk_comment_novel FOREIGN KEY (novel_id) REFERENCES novels (id),
    CONSTRAINT fk_comment_parent FOREIGN KEY (parent_id) REFERENCES comments (id)
);
