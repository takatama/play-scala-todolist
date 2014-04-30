#Tasks schema

# --- !Ups

CREATE SEQUENCE task_id_seq;
CREATE TABLE task (
  id integer NOT NULL DEFAULT nextval('task_id_seq'),
  label varchar(255),
  created date,
  finished date,
  user_id varchar(255) NOT NULL,
  deadline date
);

# --- !Downs

DROP TABLE task;
DROP SEQUENCE task_id_seq;
