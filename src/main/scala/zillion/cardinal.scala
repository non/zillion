package zillion

import spire.math.Rational

import zillion.fraction._
import zillion.Util.{Cardinal, denominator, render, isPowerOfTen}

object cardinal {

  /**
   * Returns the given number's cardinal name.
   * 
   * A cardinal number describes how many of something. For example,
   * cardinal(3) would be "three" and cardinal(27) would be
   * "twenty-seven".
   */
  def apply(n: Long): String =
    render(BigInt(n), Cardinal)
  
  /**
   * Returns the given number's cardinal name.
   * 
   * A cardinal number describes how many of something. For example,
   * cardinal(3) would be "three" and cardinal(27) would be
   * "twenty-seven".
   */
  def apply(n: BigInt): String =
    render(n, Cardinal)

  /**
   * Returns the given fraction's cardinal name.
   * 
   * The exact behavior can be configured via imports from
   * zillion.options.fractions. Here's how 21/15 would be rendered:
   * 
   *   simple: seven over five
   *   traditional: seven fifths
   */
  def apply(r: Rational, propriety: Propriety = Improper, format: Format = Heuristic): String =
    fraction(r.numerator, r.denominator, propriety, format)

  def apply(x: BigDecimal): String =
    if (x.ulp >= 1) {
      apply(x.toBigInt)
    } else {
      val n = (x / x.ulp).toBigInt
      val d = (BigDecimal(1.0) / x.ulp).toBigInt
      fraction(n, d, Mixed, Traditional)
    }

  /**
   * Returns the given fraction's cardinal name.
   * 
   * The exact behavior can be configured via imports from
   * zillion.options.fractions. Here's how 21, 15 would be rendered:
   * 
   *   simple: twenty-one over fifteen
   *   traditional: twenty-one fifteenths
   */
  def fraction(n: BigInt, d: BigInt, propriety: Propriety = Improper, format: Format = Heuristic): String =
    if (n < 0) "negative " + fraction(-n, d, propriety, format)
    else if (d == 1) apply(n)
    else (propriety, format) match {
      case (Mixed, _) if n > d =>
        apply(n / d) + " and " + fraction(n % d, d, propriety, format)
      case (_, Traditional) =>
        if (n == 1) "one " + denominator(d)
        else if (d == 2) apply(n) + " halves"
        else {
          val s = denominator(d)
          val s2 = if (s.startsWith("one ")) s.substring(4) else s
          s"${apply(n)} ${s2}s"
        }
      case (_, Simple) =>
        s"${apply(n)} over ${apply(d)}"
      case (_, Heuristic) =>
        if (d < 1000 || isPowerOfTen(d)) fraction(n, d, propriety, Traditional)
        else fraction(n, d, propriety, Simple)
    }
}
