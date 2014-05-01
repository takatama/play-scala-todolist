package controllers

import play.api._
import play.api.mvc._
import java.util.Date

case class TaskData(label: String, deadline: Option[Date], projectId: Option[Long])

object Application extends Controller with securesocial.core.SecureSocial {

  def index = Action {
    Redirect(routes.Application.tasks)
  }

  import play.api.data._
  import play.api.data.Forms._

  val taskForm = Form(
    mapping(
        "label" -> nonEmptyText,
	"deadline" -> optional(date),
	"projectId" -> optional(longNumber)
    )(TaskData.apply)(TaskData.unapply)
  )

  import models.{Task, Project}

  def tasks = SecuredAction { implicit request => {
      val userId = request.user.identityId.userId
      Ok(views.html.index(Task.all(userId), taskForm, request.user, Project.options(userId)))
    }
  }

  def newTask = SecuredAction { implicit request => {
    val userId = request.user.identityId.userId
    taskForm.bindFromRequest.fold(
      errors => BadRequest(views.html.index(Task.all(userId), errors, request.user, Project.options(userId))),
      taskData => {
        Task.create(request.user.identityId.userId, taskData.label, taskData.deadline)
	Redirect(routes.Application.tasks)
      }
    )}
  }

  def deleteTask(id: Long) = SecuredAction { implicit request => {
      Task.delete(request.user.identityId.userId, id)
      Redirect(routes.Application.tasks)
    }
  }

  def finishTask(id: Long) = SecuredAction { implicit request => {
      Task.finish(request.user.identityId.userId, id)
      Redirect(routes.Application.tasks)
    }
  }

  import play.api.libs.oauth.{RequestToken, OAuthCalculator}
  import play.api.libs.ws.WS
  import securesocial.core.SecureSocial
  import scala.concurrent._
  import ExecutionContext.Implicits.global

  def followers = SecuredAction { implicit request => {
      val oauthInfo = request.user.oAuth1Info.get
      Async {
        WS.url("https://api.twitter.com/1.1/followers/list.json").sign(
          OAuthCalculator(
            SecureSocial.serviceInfoFor(request.user).get.key,
            RequestToken(oauthInfo.token, oauthInfo.secret)
          )
        ).get.map(result => Ok(result.json))
      }
    }
  }
}
