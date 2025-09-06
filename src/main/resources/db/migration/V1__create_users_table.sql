CREATE TABLE users (
    id              SERIAL PRIMARY KEY,
    cognito_id      VARCHAR(255) NOT NULL UNIQUE,
    name            VARCHAR(255),
    email           VARCHAR(255) NOT NULL UNIQUE,
    avatar_url      VARCHAR(255),
    role            VARCHAR(50),
    created_by      VARCHAR(255),
    created_at      TIMESTAMP,
    modified_by     VARCHAR(255),
    updated_at      TIMESTAMP
);