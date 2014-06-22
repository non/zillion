package zillion

import zillion.Util.{Ordinal, render}

package object ordinal {

  /**
   * Returns the given number's ordinal name.
   * 
   * An ordinal number describes position or rank. For example,
   * cardinal(3) would be "third" and cardinal(27) would be
   * "twenty-seventh".
   */
  def integer(n: BigInt): String = render(n, Ordinal)
}
