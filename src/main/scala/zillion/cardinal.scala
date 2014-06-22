package zillion

import spire.math.Rational

import zillion.fraction._
import zillion.Util.{Cardinal, denominator, render, isPowerOfTen}

package object cardinal {
  
  /**
   * Returns the given number's cardinal name.
   * 
   * A cardinal number describes how many of something. For example,
   * cardinal(3) would be "three" and cardinal(27) would be
   * "twenty-seven".
   */
  def integer(n: BigInt): String =
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
  def rational(r: Rational, propriety: Propriety = Improper, format: Format = Heuristic): String =
    fraction(r.numerator, r.denominator, propriety, format)

  /**
   * Returns the given fraction's cardinal name.
   * 
   * The exact behavior can be configured via imports from
   * zillion.options.fractions. Here's how 21, 15 would be rendered:
   * 
   *   simple: twenty-one over fifteen
   *   traditional: twenty-one fifteenth
   */
  def fraction(n: BigInt, d: BigInt, propriety: Propriety = Improper, format: Format = Heuristic): String =
    if (n < 0) "negative " + fraction(-n, d, propriety, format)
    else if (d == 1) integer(n)
    else (propriety, format) match {
      case (Mixed, _) if n > d =>
        integer(n / d) + " and " + fraction(n % d, d, propriety, format)
      case (_, Traditional) =>
        if (n == 1) "one " + denominator(d)
        else if (d == 2) integer(n) + " halves"
        else {
          val s = denominator(d)
          val s2 = if (s.startsWith("one ")) s.substring(4) else s
          s"${integer(n)} ${s2}s"
        }
      case (_, Simple) =>
        s"${integer(n)} over ${integer(d)}"
      case (_, Heuristic) =>
        if (d < 1000 || isPowerOfTen(d)) fraction(n, d, propriety, Traditional)
        else fraction(n, d, propriety, Simple)
    }

  def decimal(x: BigDecimal): String =
    if (x.ulp >= 1) {
      integer(x.toBigInt)
    } else {
      val n = (x / x.ulp).toBigInt
      val d = (BigDecimal(1.0) / x.ulp).toBigInt
      fraction(n, d, Mixed, Traditional)
    }
}
