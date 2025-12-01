-- Add test user 'chinh1' for JWT token testing
INSERT INTO users (id, username, email, password, full_name, role, status)
VALUES
(4, 'chinh1', 'chinh1@example.com', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi', 'Chinh Nguyen', 'USER', 'ACTIVE')
ON DUPLICATE KEY UPDATE
username = VALUES(username),
email = VALUES(email),
password = VALUES(password),
full_name = VALUES(full_name),
role = VALUES(role),
status = VALUES(status);
