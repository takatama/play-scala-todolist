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

  def tasks = SecuredAction {
    Ok(views.html.index(Task.all(), taskForm))
  }

  def newTask = SecuredAction { implicit request =>
    taskForm.bindFromRequest.fold(
      errors => BadRequest(views.html.index(Task.all(), errors)),
      taskData => {
        Task.create(taskData.label)
	Redirect(routes.Application.tasks)
      }
    )
  }

  def deleteTask(id: Long) = SecuredAction {
    Task.delete(id)
    Redirect(routes.Application.tasks)
  }

  def finishTask(id: Long) = SecuredAction {
    Task.finish(id)
    Redirect(routes.Application.tasks)
  }

}
