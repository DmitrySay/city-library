INSERT INTO users(email, password, user_status)
VALUES ('admin@email.com', '$2a$10$I.mzF2XudR94yuv6vlr2S.ugj0.wsaHZUGqMh9mG1aWzwyb6BzyHq', 'ACTIVE');

INSERT INTO users_roles(user_id, role_id)
VALUES ('1', '1');

INSERT INTO users(email, password, user_status)
VALUES ('user@email.com', '$2a$10$I.mzF2XudR94yuv6vlr2S.ugj0.wsaHZUGqMh9mG1aWzwyb6BzyHq', 'ACTIVE');

INSERT INTO users_roles(user_id, role_id)
VALUES ('2', '2');
