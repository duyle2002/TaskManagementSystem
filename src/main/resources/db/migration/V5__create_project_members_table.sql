CREATE TABLE IF NOT EXISTS project_members(
    id UUID NOT NULL PRIMARY KEY default uuid_generate_v4(),
    project_id UUID NOT NULL,
    user_id UUID NOT NULL,
    role TEXT NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(),
    deleted_at TIMESTAMP WITH TIME ZONE,
    FOREIGN KEY (project_id) REFERENCES projects(id) ON DELETE CASCADE,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

-- Index --
CREATE UNIQUE INDEX idx_project_members_unique_member ON project_members(project_id, user_id) where deleted_at IS NULL;

-- Trigger --
CREATE TRIGGER trigger_project_members_updated_at
    BEFORE UPDATE ON project_members
    FOR EACH ROW
    EXECUTE PROCEDURE update_updated_at();