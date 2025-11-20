-- USERS (tác giả)
INSERT INTO users (id, username, email, password, full_name, role, status)
VALUES
(1, 'nguyenvana', 'nguyenvana@example.com', '$2a$10$7QJ8QJ8QJ8QJ8QJ8QJ8QJOeQJ8QJ8QJ8QJ8QJ8QJ8QJ8QJ8QJ8QJ8', 'Nguyễn Văn A', 'READER', 'ACTIVE'),
(2, 'tranthib', 'tranthib@example.com', '$2a$10$7QJ8QJ8QJ8QJ8QJ8QJ8QJOeQJ8QJ8QJ8QJ8QJ8QJ8QJ8QJ8QJ8QJ8', 'Trần Thị B', 'AUTHOR', 'ACTIVE'),
(3, 'admin', 'admin@example.com', '$2a$10$7QJ8QJ8QJ8QJ8QJ8QJ8QJOeQJ8QJ8QJ8QJ8QJ8QJ8QJ8QJ8QJ8QJ8', 'Administrator', 'ADMIN', 'ACTIVE');

-- CATEGORIES
INSERT INTO categories (id, name, slug) VALUES
(1, 'Công nghệ', 'cong-nghe'),
(2, 'Đời sống', 'doi-song'),
(3, 'Du lịch', 'du-lich'),
(4, 'Sách', 'sach'),
(5, 'Phim ảnh', 'phim-anh'),
(6, 'Ẩm thực', 'am-thuc'),
(7, 'Sức khỏe', 'suc-khoe');

-- TAGS
INSERT INTO tags (id, name, slug) VALUES
(1, 'AI', 'ai'),
(2, 'Blockchain', 'blockchain'),
(3, 'Metaverse', 'metaverse'),
(4, 'Công nghệ', 'cong-nghe'),
(5, 'Cuộc sống', 'cuoc-song'),
(6, 'Công việc', 'cong-viec'),
(7, 'Sức khỏe', 'suc-khoe'),
(8, 'Quản lý thời gian', 'quan-ly-thoi-gian');

-- POSTS
INSERT INTO posts (id, title, slug, content, author_id, status, view_count)
VALUES
(1, 'Những xu hướng công nghệ đáng chú ý năm 2024', 'nhung-xu-huong-cong-nghe-2024', 
'<p class="mb-6">Năm 2024 đánh dấu một bước ngoặt quan trọng trong lĩnh vực công nghệ với sự xuất hiện của nhiều xu hướng mới và đột phá.</p><h2 class="text-2xl font-bold mb-4">1. AI Generative</h2><p class="mb-6">AI Generative đã vượt xa khỏi việc chỉ tạo ra hình ảnh và văn bản.</p>', 
1, 'PUBLISHED', 2847),
(2, 'Cách cân bằng cuộc sống và công việc hiệu quả', 'cach-can-bang-cuoc-song-va-cong-viec-hieu-qua',
'<p class="mb-6">Trong thời đại hiện nay, việc cân bằng giữa công việc và cuộc sống cá nhân trở thành một thách thức lớn.</p>',
2, 'PUBLISHED', 1923);

-- POST_CATEGORIES
INSERT INTO post_categories (post_id, category_id) VALUES
(1, 1), -- Post 1: Công nghệ
(2, 2); -- Post 2: Đời sống

-- POST_TAGS
INSERT INTO post_tags (post_id, tag_id) VALUES
(1, 1), (1, 2), (1, 3), (1, 4), -- Post 1: AI, Blockchain, Metaverse, Công nghệ
(2, 5), (2, 6), (2, 7), (2, 8); -- Post 2: Cuộc sống, Công việc, Sức khỏe, Quản lý thời gian

-- COMMENTS
INSERT INTO comments (user_id, post_id, content)
VALUES
(1, 1, 'Bài viết rất hay và hữu ích! Cảm ơn tác giả đã chia sẻ.'),
(2, 2, 'Tôi đã áp dụng những tips này và thấy hiệu quả rõ rệt.');
