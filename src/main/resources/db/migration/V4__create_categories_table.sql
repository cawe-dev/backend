CREATE TABLE categories (
    id              SERIAL PRIMARY KEY,
    name            VARCHAR(255) NOT NULL UNIQUE,
    created_by      VARCHAR(255),
    created_at      TIMESTAMP,
    modified_by     VARCHAR(255),
    updated_at      TIMESTAMP
);