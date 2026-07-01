CREATE TABLE refresh_tokens
(
    id UUID PRIMARY KEY,

    user_id UUID NOT NULL,

    token_hash VARCHAR(64) NOT NULL,

    created_at TIMESTAMP WITH TIME ZONE NOT NULL,

    expires_at TIMESTAMP WITH TIME ZONE NOT NULL,

    last_used_at TIMESTAMP WITH TIME ZONE,

    revoked_at TIMESTAMP WITH TIME ZONE,

    CONSTRAINT fk_refresh_tokens_user
        FOREIGN KEY (user_id)
            REFERENCES users (id)
            ON DELETE CASCADE
);

CREATE UNIQUE INDEX idx_refresh_token_hash
    ON refresh_tokens (token_hash);

CREATE INDEX idx_refresh_token_user
    ON refresh_tokens (user_id);