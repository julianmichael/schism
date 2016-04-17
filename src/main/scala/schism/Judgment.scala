package schism

sealed trait Judgment {
  override def toString = this match {
    case ComparativeJudgment(closer, further) => s"$closer < $further"
    case AnswerJudgment(answer) => s"$answer!"
  }
}
case class ComparativeJudgment(closer: String, further: String) extends Judgment
case class AnswerJudgment(answer: String) extends Judgment
