ALTER TABLE tasks
    ADD COLUMN user_id UUID;

ALTER TABLE tasks
    ADD CONSTRAINT fk_tasks_on_user
        FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE;

