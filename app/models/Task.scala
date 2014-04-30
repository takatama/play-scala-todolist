package models

import java.util.Date;

case class Task(id: Long, label: String, created: Date, finished: Option[Date], userId: String)

object Task {

  import anorm._
  import anorm.SqlParser._

  val task = {
    get[Long]("id") ~
    get[String]("label") ~
    get[Date]("created") ~
    get[Option[Date]]("finished") ~
    get[String]("user_id") map {
      case id~label~created~finished~userId => Task(id, label, created, finished, userId)
    }
  }

  import play.api.db._
  import play.api.Play.current

  def all(userId: String): List[Task] = DB.withConnection { implicit c =>
    SQL("select * from task where user_id = {userId}").on(
      'userId -> userId
    ).as(task *)
  }

  def create(label: String, userId: String) {
    val created = new Date
    DB.withConnection { implicit c =>
      SQL("insert into task (label, created, user_id) values ({label}, {created}, {userId})").on(
        'label -> label,
	'created -> created,
	'userId -> userId
      ).executeUpdate()
    }
  }

  def delete(id: Long, userId: String) {
    DB.withConnection { implicit c =>
      SQL("delete from task where id = {id} and user_id = {userId}").on(
        'id -> id,
	'userId -> userId
      ).executeUpdate()
    }
  }

  def finish(id: Long, userId: String) {
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
