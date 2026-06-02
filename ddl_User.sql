CREATE TABLE users
(
    id            BIGINT AUTO_INCREMENT NOT NULL,
    username      VARCHAR(50)           NOT NULL,
    email         VARCHAR(100)          NOT NULL,
    password_hash VARCHAR(255)          NOT NULL,
    enabled       BIT(1)                NOT NULL,
    created_at    datetime              NOT NULL,
    updated_at    datetime              NOT NULL,
    CONSTRAINT pk_users PRIMARY KEY (id)
);

CREATE TABLE users_roles
(
    user_id    BIGINT      NOT NULL,
    roles_name VARCHAR(50) NOT NULL,
    CONSTRAINT pk_users_roles PRIMARY KEY (user_id, roles_name)
);

ALTER TABLE users
    ADD CONSTRAINT uc_users_email UNIQUE (email);

ALTER TABLE users
    ADD CONSTRAINT uc_users_username UNIQUE (username);

ALTER TABLE users_roles
    ADD CONSTRAINT fk_userol_on_role FOREIGN KEY (roles_name) REFERENCES roles (name);

ALTER TABLE users_roles
    ADD CONSTRAINT fk_userol_on_user FOREIGN KEY (user_id) REFERENCES users (id);