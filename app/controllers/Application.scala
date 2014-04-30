package controllers

import play.api._
import play.api.mvc._

case class TaskData(label: String)

object Application extends Controller with securesocial.core.SecureSocial {

  def index = Action {
    Redirect(routes.Application.tasks)
  }

  import play.api.data._
  import play.api.data.Forms._

  val taskForm = Form(
    mapping(
        "label" -> nonEmptyText
    )(TaskData.apply)(TaskData.unapply)
  )

  import models.Task

  def tasks = SecuredAction { implicit request =>
    Ok(views.html.index(Task.all(request.user.identityId.userId), taskForm, request.user))
  }

  def newTask = SecuredAction { implicit request =>
    taskForm.bindFromRequest.fold(
      errors => BadRequest(views.html.index(Task.all(request.user.identityId.userId), errors, request.user)),
      taskData => {
        Task.create(taskData.label, request.user.identityId.userId)
	Redirect(routes.Application.tasks)
      }
    )
  }

  def deleteTask(id: Long) = SecuredAction { implicit request => {
      Task.delete(id, request.user.identityId.userId)
      Redirect(routes.Application.tasks)
    }
  }

  def finishTask(id: Long) = SecuredAction { implicit request => {
      Task.finish(id, request.user.identityId.userId)
      Redirect(routes.Application.tasks)
    }
  }

}
