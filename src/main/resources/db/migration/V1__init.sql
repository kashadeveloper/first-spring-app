CREATE TABLE tasks
(
    id           UUID         NOT NULL,
    title        VARCHAR(100) NOT NULL,
    description  VARCHAR(450) NOT NULL,
    is_completed BOOLEAN      NOT NULL,
    image_id     VARCHAR(255),
    CONSTRAINT pk_tasks PRIMARY KEY (id)
);

ALTER TABLE tasks
    ADD CONSTRAINT uc_tasks_title UNIQUE (title);