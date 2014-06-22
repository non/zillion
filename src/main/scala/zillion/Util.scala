package zillion

import spire.math.Rational

private[zillion] object Util {

  // Below this value we use "short scale" thousands
  val SmallCutoff = BigInt(10).pow(33)

  // This is the smallest value that we can't name.
  val LargeCutoff = BigInt(10).pow(3003)

  /**
   * Render the given number as a cardinal/ordinal string.
   * 
   * The mode parameter determines which type of name we are
   * creating. This method is recursive. Note that even ordinal mode
   * still requires cardinal renderings in some cases (i.e. the "four"
   * in "four hundred third").
   * 
   * The naming scheme for large values was introduced by John Horton
   * Conway and Richard K. Guy [1].
   * 
   * [1] http://en.wikipedia.org/wiki/Names_of_large_numbers#Proposals_for_new_naming_system
   */
  def render(n: BigInt, mode: NameMode): String =
    if (n < 0) {
      "negative " + render(-n, mode)
    } else if (n < 10) {
      mode.ones(n.toInt)
    } else if (n < 20) {
      mode.teens(n.toInt - 10)
    } else if (n < 100) {
      val i = (n / 10).toInt
      val r = (n % 10).toInt
      if (r == 0) mode.tens(i) else Cardinal.tens(i) + "-" + mode.ones(r)
    } else if (n < 1000) {
      val h = render((n / 100).toInt, Cardinal)
      val r = (n % 100).toInt
      if (r == 0) mode.suffix(s"$h hundred") else s"$h hundred ${render(r, mode)}"
    } else if (n < 1000000) {
      val m = render((n / 1000).toInt, Cardinal)
      val r = (n % 1000).toInt
      if (r == 0) mode.suffix(s"$m thousand") else s"$m thousand ${render(r, mode)}"
    } else if (n < SmallCutoff) {
      val x = log10Mult3(n)
      val p = BigInt(10).pow(x)
      val b = render(n / p, Cardinal)
      val r = n % p
      val t = thousands(x / 3)
      if (r == 0) mode.suffix(s"$b $t") else s"$b $t ${render(r, mode)}"
    } else if (n < LargeCutoff) {
      val x = log10Mult3(n)
      val xm3 = x - 3
      val p = BigInt(10).pow(x)
      val b = render(n / p, Cardinal)
      val r = n % p
      val lh = largeHundreds(xm3 / 300)
      val lt = largeTens((xm3 % 300) / 30, false)
      val lu = largeUnits((xm3 % 30) / 3, if (lt == "") lh.last else lt.last)
      if (r == 0) mode.suffix(s"$b ${lu}${lt}${lh}llion")
      else s"$b ${lu}${lt}${lh}llion ${render(r, mode)}"
    } else {
      throw new IllegalArgumentException(s"number is >= 10^3003")
    }

  val thousands = Vector("", "thousand", "million", "billion", "trillion", "quadrillion", "quintillion", "sextillion", "septillion", "octillion", "nonillion")

  /**
   * This method is used to create the large units between 10^33
   * (below which we use the short-scale names such as thousand,
   * million, billion) and 10^3003 (the limit of names we can
   * produce).
   */
  def largeUnits(num: Int, nextc: Char): String = {

    def m(c: Char): Boolean = c match {
      case 'c' | 'o' | 'q' | 't' | 'v' => true
      case _ => false
    }

    def s(c: Char): Boolean = c match {
      case 'c' | 'd' | 'o' | 'q' | 's' | 't' | 'v' => true
      case _ => false
    }

    num match {
      case 0 => ""
      case 1 => "un"
      case 2 => "duo"
      case 3 => if (s(nextc)) "tres" else "tre"
      case 4 => "quattor"
      case 5 => "quinqua"
      case 6 => if (s(nextc)) "ses" else "se"
      case 7 => if (m(nextc)) "septem" else "septe"
      case 8 => "octo"
      case 9 => if (m(nextc)) "novem" else "nove"
    }
  }

  def largeTens(num: Int, last: Boolean): String =
    num match {
      case 0 => ""
      case 1 => "deci"
      case 2 => "viginti"
      case 3 => if (last) "triginti" else "triginta"
      case 4 => if (last) "quadraginti" else "quadraginta"
      case 5 => if (last) "quinquaginti" else "quinquaginta"
      case 6 => if (last) "sexaginti" else "sexaginta"
      case 7 => if (last) "septuaginti" else "septuaginta"
      case 8 => if (last) "octoginti" else "octoginta"
      case 9 => if (last) "nonaginti" else "nonaginta"
    }

  def largeHundreds(num: Int): String =
    num match {
      case 0 => ""
      case 1 => "centi"
      case 2 => "ducenti"
      case 3 => "trecenti"
      case 4 => "quadringenti"
      case 5 => "quingenti"
      case 6 => "sescenti"
      case 7 => "septingenti"
      case 8 => "octingenti"
      case 9 => "nongenti"
    }

  import scala.math.{ceil, floor, log}

  /**
   * Return the floor of log10(n).
   * 
   * We assume n is non-negative.
   */
  def log10(n: BigInt): Int = {
    // get a lower bound approximation for log10(n)
    var x = floor((n.bitLength - 1) * log(2) / log(10)).toInt
    var q = n / BigInt(10).pow(x)
    while (q >= 10) { x += 1; q /= 10 }
    x
  }

  /**
   * Return the largest multiple of 3 <= log10(n).
   * 
   * We assume n is non-negative.
   */
  def log10Mult3(n: BigInt): Int = {
    val m = log10(n)
    m - (m % 3)
  }

  /**
   * Naming mode, used to abstract across cardinal/ordinal.
   * 
   * This mode has some arrays for dealing with the smallest numbers
   * (whose cardinal => ordinal mappings are non-standard). It also
   * has a suffix() method which deals with all "larger" mappings.
   */
  sealed trait NameMode {
    def ones: Vector[String]
    def teens: Vector[String]
    def tens: Vector[String]
    def suffix(s: String): String
  }

  case object Cardinal extends NameMode {
    val ones = Vector("zero", "one", "two", "three", "four", "five", "six", "seven", "eight", "nine")

    val teens = Vector("ten", "eleven", "twelve", "thirteen", "fourteen", "fifteen", "sixteen", "seventeen", "eighteen", "nineteen")

    val tens = Vector("", "", "twenty", "thirty", "forty", "fifty", "sixty", "seventy", "eighty", "ninety")

    def suffix(s: String): String = s
  }

  case object Ordinal extends NameMode {
    val ones = Vector("zeroth", "first", "second", "third", "fourth", "fifth", "sixth", "seventh", "eighth", "ninth")

    val teens = Vector("tenth", "eleventh", "twelfth", "thirteenth", "fourteenth", "fifteenth", "sixteenth", "seventeenth", "eighteenth", "nineteenth")

    val tens = Vector("", "", "twentieth", "thirtieth", "fortieth", "fiftieth", "sixtieth", "seventieth", "eightieth", "ninetieth")
    def suffix(s: String): String = s + "th"
  }

  import fraction._

  def isPowerOfTen(x: BigInt): Boolean = {
    val (q, r) = x /% 10
    if (r != 0) false
    else if (q == 1) true
    else isPowerOfTen(q)
  }

  def denominator(n: BigInt): String =
    if (n < 0) denominator(-n)
    else if (n == 0) throw new IllegalArgumentException("/0")
    else if (n == 1) ""
    else if (n == 2) "half"
    else zillion.ordinal.integer(n)
}
