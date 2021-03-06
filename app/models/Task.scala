package models

import anorm._
import anorm.SqlParser._

import play.api.db._
import play.api.Play.current

import java.util.Date;

case class Task(id: Long, label: String, created: Date, finished: Option[Date], userId: String, deadline: Option[Date], projectId: Option[Long])

object Task {
  val task = {
    get[Long]("id") ~
    get[String]("label") ~
    get[Date]("created") ~
    get[Option[Date]]("finished") ~
    get[String]("user_id") ~
    get[Option[Date]]("deadline") ~
    get[Option[Long]]("project_id") map {
      case id~label~created~finished~userId~deadline~projectId => Task(id, label, created, finished, userId, deadline, projectId)
    }
  }

  def all(userId: String): List[Task] = DB.withConnection { implicit c =>
    SQL("select * from task where user_id = {userId}").on(
      'userId -> userId
    ).as(task *)
  }

  def create(userId: String, label: String, deadline: Option[Date] = None, projectId: Option[Long] = None) {
    val created = new Date
    DB.withConnection { implicit c =>
      SQL("insert into task (label, created, user_id, deadline, project_id) values ({label}, {created}, {userId}, {deadline}, {projectId})").on(
        'label -> label,
	'created -> created,
	'userId -> userId,
	'deadline -> deadline,
	'projectId -> projectId
      ).executeUpdate()
    }
  }

  def delete(userId: String, id: Long) {
    DB.withConnection { implicit c =>
      SQL("delete from task where id = {id} and user_id = {userId}").on(
        'id -> id,
	'userId -> userId
      ).executeUpdate()
    }
  }

  def finish(userId: String, id: Long) {
    val finished = new Date
    DB.withConnection { implicit c =>
      SQL("update task set finished = {finished} where id = {id} and user_id = {userId}").on(
        'finished -> finished,
	'id -> id,
	'userId -> userId
      ).executeUpdate()
    }
  }

}
