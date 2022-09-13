create table users
(
    id       BIGSERIAL PRIMARY KEY,
    email    VARCHAR(255) not null unique,
    password VARCHAR(255) not null
);

CREATE TABLE permissions
(
    id   BIGSERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL
);

CREATE TABLE roles
(
    id   BIGSERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL
);

CREATE TABLE roles_permissions
(
    role_id       BIGINT NOT NULL,
    permission_id BIGINT NOT NULL,
    CONSTRAINT roles_permissions_fk_1 FOREIGN KEY (role_id) REFERENCES roles (id),
    CONSTRAINT roles_permissions_fk_2 FOREIGN KEY (permission_id) REFERENCES permissions (id)
);

CREATE table users_roles
(
    user_id BIGINT NOT NULL,
    role_id BIGINT NOT NULL,
    CONSTRAINT users_roles_fk_1 FOREIGN KEY (user_id) REFERENCES users (id),
    CONSTRAINT users_roles_fk_2 FOREIGN KEY (role_id) REFERENCES roles (id)
);




