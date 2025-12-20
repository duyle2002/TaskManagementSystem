CREATE TABLE IF NOT EXISTS projects(
    id UUID NOT NULL PRIMARY KEY default uuid_generate_v4(),
    name TEXT NOT NULL,
    description TEXT,
    owner_id UUID NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(),
    deleted_at TIMESTAMP WITH TIME ZONE,
    FOREIGN KEY (owner_id) REFERENCES users(id) ON DELETE CASCADE
);

-- Index --
CREATE INDEX idx_projects_by_owner_id ON projects(owner_id) where deleted_at IS NULL;
CREATE UNIQUE INDEX idx_projects_by_name ON projects(name) where deleted_at IS NULL;

-- Trigger --
CREATE TRIGGER trigger_projects_updated_at
    BEFORE UPDATE ON projects
    FOR EACH ROW
    EXECUTE PROCEDURE update_updated_at();