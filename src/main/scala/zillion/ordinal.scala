package zillion

import zillion.Util.{Cardinal, Ordinal, render}
import spire.math.SafeLong

object ordinal {

  /**
   * Returns the given number's ordinal name.
   *
   * An ordinal number describes position or rank. For example,
   * cardinal(3) would be "third" and cardinal(27) would be
   * "twenty-seventh".
   */
  def apply(n: SafeLong): String =
    render(n, Ordinal)

  /**
   * Returns the given number's ordinal name.
   *
   * An ordinal number describes position or rank. For example,
   * cardinal(3) would be "third" and cardinal(27) would be
   * "twenty-seventh".
   */
  def apply(n: Long): String =
    apply(SafeLong(n))

  /**
   * Returns the given number's ordinal name.
   *
   * An ordinal number describes position or rank. For example,
   * cardinal(3) would be "third" and cardinal(27) would be
   * "twenty-seventh".
   */
  def apply(n: BigInt): String =
    apply(SafeLong(n))

}
