package controllers

import play.api._
import play.api.mvc._

case class ProjectData(name: String)

object ProjectController extends Controller with securesocial.core.SecureSocial {

  /*
  def index = Action {
    Redirect(routes.ProjectController.projects)
  }
  */

  import play.api.data._
  import play.api.data.Forms._

  val projectForm = Form(
    mapping(
        "name" -> nonEmptyText
    )(ProjectData.apply)(ProjectData.unapply)
  )

  import models.Project

  def projects = SecuredAction { implicit request =>
    Ok(views.html.projects(Project.all(request.user.identityId.userId), projectForm, request.user))
  }

  def newProject = SecuredAction { implicit request =>
    projectForm.bindFromRequest.fold(
      errors => BadRequest(views.html.projects(Project.all(request.user.identityId.userId), errors, request.user)),
      projectData => {
        Project.create(request.user.identityId.userId, projectData.name)
	Redirect(routes.ProjectController.projects)
      }
    )
  }

  def deleteProject(id: Long) = SecuredAction { implicit request => {
      Project.delete(request.user.identityId.userId, id)
      Redirect(routes.ProjectController.projects)
    }
  }

}
