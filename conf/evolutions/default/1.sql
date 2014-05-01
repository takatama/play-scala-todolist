# --- !Ups

CREATE SEQUENCE project_id_seq;
CREATE TABLE projects (
  id integer NOT NULL DEFAULT nextval('project_id_seq'),
  name varchar(255),
  user_id varchar(255) NOT NULL
);

CREATE SEQUENCE task_id_seq;
CREATE TABLE task (
  id integer NOT NULL DEFAULT nextval('task_id_seq'),
  label varchar(255),
  created date,
  finished date,
  user_id varchar(255) NOT NULL,
  deadline date,
  project_id integer,
  foreign key(project_id) references projects(id) on delete cascade
);

# --- !Downs

DROP TABLE projects;
DROP SEQUENCE project_id_seq;

DROP TABLE task;
DROP SEQUENCE task_id_seq;
