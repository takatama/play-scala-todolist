import org.specs2.mutable._
import org.specs2.runner._
import org.junit.runner._

import play.api.test._
import play.api.test.Helpers._

import models._

@RunWith(classOf[JUnitRunner])
class ProjectSpec extends Specification {

  "Project" should {

    "have a name" in new WithApplication {
      val userId = "1"
      Project.all(userId).length must beEqualTo(0)
      val name = "the name"
      Project.create(userId, name)
      Project.all(userId).last.name must beEqualTo(name)
    }
  }

  "Project#delete" should {
    "delete already created project" in new WithApplication {
      val userId = "1"
      Project.create(userId, "the name")
      Project.all(userId).length must beEqualTo(1)
      val id = Project.all(userId).last.id
      Project.delete(userId, id)
      Project.all(userId).length must beEqualTo(0)
    }
  }

  "Project#options" should {
    "return (id, name) pairs" in new WithApplication {
      val userId = "1"
      val name = "the name"
      Project.create(userId, name)
      Project.all(userId).length must beEqualTo(1)
      Project.options(userId) must beEqualTo(Seq("1" -> name))
    }
  }

  "Project#addMember" should {
    "add a member to the project" in new WithApplication {
      val userId = "1"
      val memberId = "2"
      Project.create(userId, "the name")
      val projectId = Project.all(userId).last.id
      Project.addMember(userId, projectId, memberId)
      Project.isMember(memberId, projectId) must beTrue
    }
  }

  "Project#removeMember" should {
    "remove a member who is not the current user from the project" in new WithApplication {
      val userId = "1"
      val memberId = "2"
      val projectId = Project.create(userId, "the name").id
      Project.addMember(userId, projectId, memberId)
      Project.isMember(memberId, projectId) must beTrue
      Project.removeMember(userId, projectId, memberId)
      Project.isMember(memberId, projectId) must beFalse
    }

    "not remove the owner user from the project" in new WithApplication {
      val userId = "1"
      val memberId = "1"
      val projectId = Project.create(userId, "the name").id
      Project.isMember(memberId, projectId) must beTrue
      Project.removeMember(userId, projectId, memberId)
      Project.isMember(memberId, projectId) must beTrue
    }
  }

}
