package models

import anorm._
import anorm.SqlParser._

import play.api.db._
import play.api.Play.current

case class Project(id: Long, name: String, userId: String)

object Project {
  val project = {
    get[Long]("id") ~
    get[String]("name") ~
    get[String]("user_id") map {
      case id~name~userId => Project(id, name, userId)
    }
  }

  def all(userId: String): List[Project] = DB.withConnection { implicit c =>
    SQL("select * from projects where user_id = {userId}").on(
      'userId -> userId
    ).as(project *)
  }

  def create(userId: String, name: String) {
    DB.withConnection { implicit c =>
      SQL("insert into projects (name, user_id) values ({name}, {userId})").on(
        'name -> name,
	'userId -> userId
      ).executeUpdate()
    }
  }

  def delete(userId: String, id: Long) {
    DB.withConnection { implicit c =>
      SQL("delete from projects where id = {id} and user_id = {userId}").on(
        'id -> id,
	'userId -> userId
      ).executeUpdate()
    }
  }

  def options(userId: String): Seq[(String, String)] = DB.withConnection { implicit connection =>
    SQL("select * from projects where user_id = {userId}").on(
      'userId -> userId
    ).as(Project.project *).map(c => c.id.toString -> c.name)
  }
}
