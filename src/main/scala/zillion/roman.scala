package zillion

object roman {

  /**
   * Return a non-negative Long as a roman numeral.
   *
   * There are some places where roman numerals are not totally
   * standardized. For example Zillion renders 0 as Nulla.
   *
   * Similarly, very large numbers are rendered using overbars (each
   * overbar multiplies the value by 1000). Zillion renders overbars
   * using [...], so [XI] represents eleven thousand. Multiple
   * overbars are possible: [[XI]] represents eleven million.
   */
  def apply(n: Long): String = {

    // we assume here that n > 0.
    def recurse(n: Long): String =
      if (n <= 3888L) {
        render(n)
      } else {
        val q = n / 1000L
        val r = n % 1000L
        if (q == 0L) render(r)
        else if (r == 0L) "[" + recurse(q) + "]"
        else "[" + recurse(q) + "]" + render(r)
      }

    // we assume here that n >= 0 and n <= 3888.
    def render(n: Long): String =
      if (n >= 1000L) "M" + render(n - 1000L)
      else if (n >= 900L) "CM" + render(n - 900L)
      else if (n >= 500L) "D" + render(n - 500L)
      else if (n >= 400L) "CD" + render(n - 400L)
      else if (n >= 100L) "C" + render(n - 100L)
      else if (n >= 90L) "XC" + render(n - 90L)
      else if (n >= 50L) "L" + render(n - 50L)
      else if (n >= 40L) "XL" + render(n - 40L)
      else if (n >= 10L) "X" + render(n - 10L)
      else if (n >= 9L) "IX" + render(n - 9L)
      else if (n >= 5L) "V" + render(n - 5L)
      else if (n >= 4L) "IV" + render(n - 4L)
      else if (n >= 1L) "I" + render(n - 1L)
      else ""

    if (n > 0L) recurse(n)
    else if (n == 0L) "Nulla"
    else throw new IllegalArgumentException(s"$n is negative!")
  }
}
