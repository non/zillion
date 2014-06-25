## Zillion

### Overview

Zillion is a simple Scala package for producing English names for
numbers.

Zillion can currently produce cardinal or ordinal names for all
numbers above -10^3003 and below 10^3003.

```scala
import zillion.{cardinal, ordinal}

cardinal(3)     // "three"
cardinal(101)   // "one hundred one"
cardinal(13579) // "thirteen thousand five hundred seventy-nine"
cardinal(-12)   // "negative twelve"

ordinal(0)      // "zeroth"
ordinal(20)     // "twentieth"
ordinal(117)    // "one hundred seventeenth"
ordinal(9999)   // "nine thousand nine hundred ninety-ninth"

cardinal(BigInt(10).pow(123) + BigInt(10).pow(37) + BigInt(1234))
// "one quadragintallion ten undecillion one thousand two hundred thirty-four"

cardinal(BigInt(10).pow(300) * 999)
// "nine hundred ninety-nine novenonagintallion"
```

Zillion can also produce cardinal names for fractions and decimal
numbers:

```scala
import spire.math.Rational

cardinal(Rational(14, 27))      // fourteen twenty-sevenths
cardinal.fraction(10, 25)       // ten twenty-fifths
cardinal(BigDecimal("1247.23")) // one thousand two hundred forty-seven and twenty-three hundredths
```

### Getting Zillion

Zillion is published to [bintray](https://bintray.com/) using the
[bintray-sbt](https://github.com/softprops/bintray-sbt) plugin.

Zillion supports Scala 2.10.x and 2.11.x. If you use SBT, you can
include Zillion via the following `build.sbt` snippets:

```
resolvers += "bintray/meetup" at "http://dl.bintray.com/meetup/maven"

libraryDependencies += "org.spire-math" %% "zillion" % "0.1.0"
```

### Details

The [strategy](http://en.wikipedia.org/wiki/Names_of_large_numbers#Proposals_for_new_naming_system)
for naming large numbers comes from John Horton Conway and Richard
K. Guy by way of Wikipedia.

This library has not yet been extensively tested or reviewed. I'm not
sure what the best strategy is for unit-testing the very large number
names, other than spot checks and regression tests.

### Future Work

It would be great to support numbers larger than 10^3003.

Right now the the underlying rendering uses `BigInt` . It might be
worth trying to support `Long` directly (to avoid creating unnecessary
`BigInt` instances)e.

It could also be nice to allow pluggable capitalization and grammar
rules. Currently all names are lowercase, and no conjunctions are
used.

Zillion only supports English. I'm not sure how useful this code would
be to rendering number names in other languages. But I'd love to hear
from folks who would be interested in trying to support other
languages.

### Copyright and License

All code is available to you under the MIT license, available at
http://opensource.org/licenses/mit-license.php and also in the
[COPYING](COPYING) file.

Copyright Erik Osheim, 2014.
