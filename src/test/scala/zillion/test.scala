package zillion

import org.scalatest.matchers.ShouldMatchers
import org.scalatest._
import prop._

import org.scalacheck.{Arbitrary, Gen}
import Arbitrary.arbitrary

class CardinalTest extends GenericTest {
  def render(n: BigInt): String = cardinal(n)
}

class OrdinalTest extends GenericTest {
  def render(n: BigInt): String = ordinal(n)
}

trait GenericTest extends PropSpec with Matchers with GeneratorDrivenPropertyChecks {

  def render(n: BigInt): String

  def checkModK(n: BigInt, k: Int) {
    val m = n.abs % k
    if (m != 0) render(n).endsWith(render(m)) shouldBe true
  }

  property("last digit") {
    forAll { (n: BigInt) =>
      val m = n.abs % 100
      if (m < 10 || m > 20) checkModK(n, 10)
    }
  }

  property("last two digits") {
    forAll { (n: BigInt) => checkModK(n, 100) }
  }

  property("last three digits") {
    forAll { (n: BigInt) => checkModK(n, 1000) }
  }
}
