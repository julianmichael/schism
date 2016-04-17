package schism

import scala.util.Random

trait Guesser {
  def guess: Query
  def learn(j: Judgment): Guesser = j match {
    case AnswerJudgment(_) => this // doesn't matter; game is over
    case cj@ComparativeJudgment(_, _) => learnFromComparison(cj)
  }
  def learnFromComparison(cj: ComparativeJudgment): Guesser
}

case class RandomNonRepeatingGuesser(vocabulary: Set[String],
                                     guessed: Set[String]
) extends Guesser {
  private[this] def sampleWord(set: Set[String]): Option[String] =
    if(set.isEmpty) None
    else Some(set.toVector(Random.nextInt(set.size)))

  override def guess = {
    val firstSupport = vocabulary -- guessed
    sampleWord(firstSupport) match {
      case None => Capitulation
      case Some(firstWord) =>
        val secondSupport = firstSupport - firstWord
        sampleWord(secondSupport) match {
          case None => ComparativeQuery(firstWord, firstWord)
          case Some(secondWord) => ComparativeQuery(firstWord, secondWord)
        }
    }
  }

  override def learnFromComparison(cj: ComparativeJudgment) = cj match {
    case ComparativeJudgment(closer, further) =>
      RandomNonRepeatingGuesser(vocabulary, guessed + closer + further)
  }
}
