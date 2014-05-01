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

  def create(userId: String, name: String): Project = {
    DB.withTransaction { implicit connection =>
      val projectId: Long = {
        SQL("select next value for project_id_seq").as(scalar[Long].single)
      }
      
      SQL("insert into projects (id, name, user_id) values ({projectId}, {name}, {userId})").on(
        "projectId" -> projectId,
        'name -> name,
	'userId -> userId
      ).executeUpdate()
      
      SQL("insert into project_member (project_id, user_id) values ({projectId}, {userId})").on(
        "projectId" -> projectId,
	"userId" -> userId
      ).executeUpdate()
      
      Project(projectId, name, userId)
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

  def isMember(userId: String, projectId: Long): Boolean = {
    DB.withConnection { implicit connection =>
      SQL(
        """
	  select count(project_member.project_id) = 1 from project_member
	  where project_member.user_id = {userId} and project_member.project_id = {projectId}
	"""
      ).on(
	"userId" -> userId,
        "projectId" -> projectId
      ).as(scalar[Boolean].single)
    }
  }

  def addMember(userId:String, projectId: Long, memberId: String) {
    if (userId != memberId && isMember(userId, projectId)) {
      DB.withConnection { implicit connection =>
        SQL("insert into project_member (project_id, user_id) values ({projectId}, {memberId})").on(
          "projectId" -> projectId,
          "memberId" -> memberId
        ).executeUpdate()
      }
    }
  }

  def removeMember(userId:String, projectId: Long, memberId: String) {
    if (userId != memberId && isMember(userId, projectId)) {
      DB.withConnection { implicit connection =>
        SQL("delete from project_member where project_id = {projectId} and user_id = {memberId}").on(
          "projectId" -> projectId,
          "memberId" -> memberId
        ).executeUpdate()
      }
    }
  }

}
