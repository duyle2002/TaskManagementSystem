CREATE TABLE IF NOT EXISTS refresh_tokens(
    id UUID NOT NULL PRIMARY KEY default uuid_generate_v4(),
    user_id UUID NOT NULL,
    token TEXT NOT NULL,
    expires_at TIMESTAMP WITH TIME ZONE NOT NULL,
    revoked_at TIMESTAMP WITH TIME ZONE,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(),
    deleted_at TIMESTAMP WITH TIME ZONE,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

-- Index --
CREATE INDEX idx_refresh_token_by_user_id ON refresh_tokens(user_id) where deleted_at IS NULL;
CREATE INDEX idx_refresh_token_by_token ON refresh_tokens(token) where deleted_at IS NULL;

-- Trigger --
CREATE TRIGGER trigger_refresh_token_updated_at
    BEFORE UPDATE ON refresh_tokens
    FOR EACH ROW
    EXECUTE PROCEDURE update_updated_at();