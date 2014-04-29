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
      Task.all().length must beEqualTo(0)
      val label = "the label"
      Task.create(label)
      Task.all().last.label must beEqualTo(label)
    }
    
    "have a created date" in new WithApplication {
      Task.all().length must beEqualTo(0)
      Task.create("a label")
      Task.all().last.created must not beNull
    }
  }
}