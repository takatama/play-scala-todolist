import org.specs2.mutable._
import org.specs2.runner._
import org.junit.runner._

import play.api.test._
import play.api.test.Helpers._

import models._

@RunWith(classOf[JUnitRunner])
class TaskSpec extends Specification {

  "Task" should {

    "be with a label" in new WithApplication{
      Task.all().length must beEqualTo(0)
      val label = "the label"
      Task.create(label)
      Task.all().last.label must beEqualTo(label)
    }
  }
}
