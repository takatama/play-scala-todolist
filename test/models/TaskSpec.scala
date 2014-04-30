import org.specs2.mutable._
import org.specs2.runner._
import org.junit.runner._

import play.api.test._
import play.api.test.Helpers._

import models._

@RunWith(classOf[JUnitRunner])
class TaskSpec extends Specification {

  "Task" should {

    "have a label" in new WithApplication {
      val userId = "1"
      Task.all(userId).length must beEqualTo(0)
      val label = "the label"
      Task.create(label, userId)
      Task.all(userId).last.label must beEqualTo(label)
    }
    
    "have a created date" in new WithApplication {
      val userId = "1"
      Task.all(userId).length must beEqualTo(0)
      Task.create("a label", userId)
      Task.all(userId).last.created must not beNull
    }

    "have a None finished date after creation" in new WithApplication {
      val userId = "1"
      Task.all(userId).length must beEqualTo(0)
      Task.create("a label", userId)
      Task.all(userId).last.finished must beNone
    }
  }

  "Task#finish" should {
    "make finished not null" in new WithApplication {
      val userId = "1"
      Task.create("a label", userId)
      val task = Task.all(userId).last
      task.finished must beNone
      Task.finish(task.id, userId)
      Task.all(userId).last.finished must beSome
    }
  }
}
