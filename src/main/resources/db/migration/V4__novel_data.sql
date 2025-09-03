-- GENRES
INSERT INTO genres (id, name, slug) VALUES
(1, 'Fantasy', 'fantasy'),
(2, 'Game', 'game');

-- NOVELS
INSERT INTO novels (id, title, slug, description, author_id, status, view_count, avg_rating)
VALUES
(1, 'Hành Trình Đến Với Ánh Sáng', 'hanh-trinh-den-voi-anh-sang', 'Câu chuyện về một chàng trai trẻ tên Minh khám phá thế giới ma thuật đầy bí ẩn...', 1, 'ONGOING', 25420, 4.8),
(2, 'Vương Quốc Bóng Đêm', 'vuong-quoc-bong-dem', 'Một câu chuyện về cuộc chiến giữa ánh sáng và bóng tối trong một vương quốc cổ đại...', 2, 'COMPLETED', 18750, 4.6),
(3, 'Thế Giới Game', 'the-gioi-game', 'Một game thủ chuyên nghiệp bỗng nhiên bị cuốn vào thế giới game mà anh từng chơi...', 1, 'ONGOING', 12340, 4.4);

-- NOVEL_GENRES
INSERT INTO novel_genres (novel_id, genre_id) VALUES
(1, 1),
(2, 1),
(3, 2);

-- CHAPTERS
INSERT INTO chapters (id, novel_id, title, content, chapter_number, view_count)
VALUES
(1, 1, 'Chương 1: Khởi đầu của hành trình', '<p class="mb-6">Minh ngồi bên cửa sổ, nhìn ra ngoài khu vườn nhỏ của gia đình... (rút gọn)</p>', 1, 2234),
(2, 1, 'Chương 2: Phát hiện sức mạnh', '<p class="mb-6">Sau đêm đó, Minh không thể ngủ được... (rút gọn)</p>', 2, 2156),
(3, 1, 'Chương 3: Người thầy đầu tiên', '<p class="mb-6">Sau cuộc gặp gỡ với người đàn ông áo đen, Minh cảm thấy cần phải học cách kiểm soát sức mạnh của mình... (rút gọn)</p>', 3, 2089);

-- COMMENTS cho chapter (giả lập user_id = 1, 2, 3)
INSERT INTO comments (user_id, novel_id, content)
VALUES
(1, 1, 'Chương này hay quá! Cách tác giả miêu tả việc khám phá sức mạnh ma thuật rất sinh động.'),
(2, 1, 'Minh thật sự có tài năng tự nhiên. Không thể chờ đợi để xem anh sẽ phát triển như thế nào.'),
(2, 1, 'Thầy Lâm thật sự là một nhân vật thú vị. Hy vọng sẽ thấy nhiều hơn về ông ấy.');
