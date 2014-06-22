package zillion.fraction

sealed trait Propriety
case object Improper extends Propriety
case object Mixed extends Propriety

sealed trait Format
case object Simple extends Format
case object Traditional extends Format
case object Heuristic extends Format
