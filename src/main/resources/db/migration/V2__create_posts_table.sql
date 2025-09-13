CREATE TABLE posts (
    id              SERIAL PRIMARY KEY,
    user_id         VARCHAR(255) NOT NULL,
    title           VARCHAR(255) NOT NULL UNIQUE,
    created_by      VARCHAR(255),
    created_at      TIMESTAMP,
    modified_by     VARCHAR(255),
    updated_at      TIMESTAMP
);