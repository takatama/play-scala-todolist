import org.specs2.mutable._
import org.specs2.runner._
import org.junit.runner._

import play.api.test._
import play.api.test.Helpers._

import models._

import java.util.Date

@RunWith(classOf[JUnitRunner])
class TaskSpec extends Specification {

  "Task" should {

    "have a label" in new WithApplication {
      val userId = "1"
      Task.all(userId).length must beEqualTo(0)
      val label = "the label"
      Task.create(userId, label)
      Task.all(userId).last.label must beEqualTo(label)
    }
    
    "have a created date" in new WithApplication {
      val userId = "1"
      Task.all(userId).length must beEqualTo(0)
      Task.create(userId, "a label")
      Task.all(userId).last.created must not beNull
    }

    "have a None finished date after creation" in new WithApplication {
      val userId = "1"
      Task.all(userId).length must beEqualTo(0)
      Task.create(userId, "a label")
      Task.all(userId).last.finished must beNone
    }
  }

  "Task#create" should {
    "have a deadline date if deadline is provided" in new WithApplication {
      val userId = "1"
      Task.create(userId, "a label", Some(new Date))
      Task.all(userId).last.deadline must beSome
    }
  }

  "Task#delete" should {
    "make delete created task" in new WithApplication {
      val userId = "1"
      Task.create(userId, "a label")
      Task.all(userId).length must beEqualTo(1)
      val id = Task.all(userId).last.id
      Task.delete(userId, id)
      Task.all(userId).length must beEqualTo(0)
    }
  }

  "Task#finish" should {
    "make finished not null" in new WithApplication {
      val userId = "1"
      Task.create(userId, "a label")
      val task = Task.all(userId).last
      task.finished must beNone
      Task.finish(userId, task.id)
      Task.all(userId).last.finished must beSome
    }
  }
}
