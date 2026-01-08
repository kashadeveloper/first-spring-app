ALTER TABLE tasks
ADD COLUMN priority VARCHAR(20);

UPDATE tasks SET priority = 'LOW' WHERE priority IS NULL;

ALTER TABLE tasks
ALTER COLUMN priority SET NOT NULL;