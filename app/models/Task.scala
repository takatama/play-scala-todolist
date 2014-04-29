package models

import java.util.Date;

case class Task(id: Long, label: String, created: Date)

object Task {

  import anorm._
  import anorm.SqlParser._

  val task = {
    get[Long]("id") ~
    get[String]("label") ~
    get[Date]("created") map {
      case id~label~created => Task(id, label, created)
    }
  }

  import play.api.db._
  import play.api.Play.current

  def all(): List[Task] = DB.withConnection { implicit c =>
    SQL("select * from task").as(task *)
  }

  def create(label: String) {
    val created = new Date
    DB.withConnection { implicit c =>
      SQL("insert into task (label, created) values ({label}, {created})").on(
        'label -> label,
	'created -> created
      ).executeUpdate()
    }
  }

  def delete(id: Long) {
    DB.withConnection { implicit c =>
      SQL("delete from task where id = {id}").on(
        'id -> id
      ).executeUpdate()
    }
  }

}
