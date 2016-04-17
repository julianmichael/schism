package schism

sealed trait Query
case class ComparativeQuery(word1: String, word2: String) extends Query
case object Capitulation extends Query
