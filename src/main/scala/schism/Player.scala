package schism

import scala.concurrent._
import java.util.Scanner

trait Player {
  def respond(query: Query): Future[Judgment]
}

case class CommandLinePlayer(
  vocabulary: Set[String],
  input: Scanner
)(implicit ec: ExecutionContext
) extends Player {
  override def respond(query: Query) = query match {
    case ComparativeQuery(w1, w2) =>
      println(s"Is it closer to (a) $w1, or (b) $w2? (If it is the correct answer, add c to your response.)")
      Future {
        lazy val responses: Stream[String] = input.nextLine() #:: responses
        def responseIsValid(r: String) = r.equalsIgnoreCase("a") ||
          r.equalsIgnoreCase("b") ||
          r.equalsIgnoreCase("ac") ||
          r.equalsIgnoreCase("bc")
        val response = responses.dropWhile(r => !responseIsValid(r)).head.toLowerCase
        val (closer, further) = if(response.startsWith("a")) (w1, w2) else (w2, w1)
        if(response.endsWith("c")) AnswerJudgment(closer)
        else ComparativeJudgment(closer, further)
      }
    case Capitulation =>
      println("I give up. What was the word?")
      Future {
        lazy val responses: Stream[String] = input.nextLine() #:: responses
        def responseIsValid(r: String) = vocabulary.contains(r.toLowerCase)
        val response = responses.dropWhile(r => !responseIsValid(r)).head.toLowerCase
        AnswerJudgment(response)
      }
  }
}
