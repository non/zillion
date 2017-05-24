package zillion

import org.scalatest._
import prop._
import org.scalacheck.{Arbitrary, Gen}
import Arbitrary.arbitrary
import spire.math.SafeLong

case class Exponent(value: Int) {
  require(0 <= value && value <= 3003)
}

object Exponent {
  implicit val arbitraryExponent: Arbitrary[Exponent] =
    Arbitrary(Gen.choose(0, 3003).map(Exponent(_)))
}

class CardinalTest extends GenericTest {
  def render(n: SafeLong): String = cardinal(n)
}

class OrdinalTest extends GenericTest {
  def render(n: SafeLong): String = ordinal(n)
}

trait GenericTest extends PropSpec with Matchers with PropertyChecks {

  implicit lazy val arbitrarySafeLong: Arbitrary[SafeLong] =
    Arbitrary(arbitrary[BigInt].map(SafeLong(_)))

  def render(n: SafeLong): String

  def checkModK(n: SafeLong, k: Int) {
    val m = n.abs % k
    if (m != 0) render(n).endsWith(render(m)) shouldBe true
  }

  property("big numbers don't crash") {
    forAll { (k: Exponent, offset: SafeLong, b: Boolean) =>
      val big = SafeLong(10).pow(k.value)
      render(big - offset)
      render(offset - big)
    }
  }

  property("negative numbers are consistent") {
    forAll { (n: SafeLong) =>
      val (x, y) = if (n < 0) (-n, n) else (n, -n)
      val sx = render(x)
      val sy = render(y)
      if (sx == sy) n shouldBe 0
      else {
        sy.startsWith("negative ") shouldBe true
        sy.substring(9) shouldBe sx
      }
    }
  }

  property("last digit") {
    forAll { (n: SafeLong) =>
      val m = n.abs % 100
      if (m < 10 || m > 20) checkModK(n, 10)
    }
  }

  property("last two digits") {
    forAll { (n: SafeLong) => checkModK(n, 100) }
  }

  property("last three digits") {
    forAll { (n: SafeLong) => checkModK(n, 1000) }
  }

  property("Int and SafeLong match") {
    forAll { (n: Int) => render(n) shouldBe render(SafeLong(n)) }
  }
}
