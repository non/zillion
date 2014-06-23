package zillion

import zillion.Util.{Cardinal, Ordinal, render}

object ordinal {

  /**
   * Returns the given number's ordinal name.
   * 
   * An ordinal number describes position or rank. For example,
   * cardinal(3) would be "third" and cardinal(27) would be
   * "twenty-seventh".
   */
  def apply(n: Long): String =
    render(BigInt(n), Cardinal)

  /**
   * Returns the given number's ordinal name.
   * 
   * An ordinal number describes position or rank. For example,
   * cardinal(3) would be "third" and cardinal(27) would be
   * "twenty-seventh".
   */
  def apply(n: BigInt): String = render(n, Ordinal)
}
