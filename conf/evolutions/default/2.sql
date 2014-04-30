#Project schema

# --- !Ups

CREATE SEQUENCE project_id_seq;
CREATE TABLE projects (
  id integer NOT NULL DEFAULT nextval('project_id_seq'),
  name varchar(255),
  user_id varchar(255) NOT NULL
);

# --- !Downs

DROP TABLE projects;
DROP SEQUENCE project_id_seq;
