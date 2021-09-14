-- ROLES
INSERT INTO roles(id, name)
VALUES ('1', 'ROLE_ADMIN');
INSERT INTO roles(id, name)
VALUES ('2', 'ROLE_USER');

-- PERMISSIONS
INSERT INTO permissions(id, name)
VALUES ('1', 'AUTHORITY_ALL');
INSERT INTO permissions(id, name)
VALUES ('2', 'AUTHORITY_CREATE');
INSERT INTO permissions(id, name)
VALUES ('3', 'AUTHORITY_READ');
INSERT INTO permissions(id, name)
VALUES ('4', 'AUTHORITY_UPDATE');
INSERT INTO permissions(id, name)
VALUES ('5', 'AUTHORITY_DELETE');

-- ADMIN
INSERT INTO roles_permissions(role_id, permission_id)
VALUES ('1', '1');

-- USER
INSERT INTO roles_permissions(role_id, permission_id)
VALUES ('2', '3');
