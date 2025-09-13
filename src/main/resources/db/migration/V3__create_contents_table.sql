CREATE TABLE contents (
    id              SERIAL PRIMARY KEY,
    post_id         VARCHAR(255) NOT NULL,
    content_order   SMALLINT NOT NULL,
    type            VARCHAR(255) NOT NULL,
    value           JSONB NOT NULL,
    created_by      VARCHAR(255),
    created_at      TIMESTAMP,
    modified_by     VARCHAR(255),
    updated_at      TIMESTAMP
);