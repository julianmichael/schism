package schism

import scala.concurrent._
import scala.concurrent.duration._

trait Game {

  val vocabulary: Set[String]
  val initialGuesser: Guesser
  def makePlayer()(implicit ec: ExecutionContext): Player
  def initialize(): Unit

  def play()(implicit ec: ExecutionContext) = {
    initialize()

    val player = makePlayer()

    def judgmentsAfter(guesser: Guesser): Future[Stream[Judgment]] = for {
      nextJudgment <- player.respond(guesser.guess)
      allFollowingJudgments <- nextJudgment match {
        case AnswerJudgment(_) => Future.successful(Stream.empty[Judgment])
        case ComparativeJudgment(_, _) => judgmentsAfter(guesser.learn(nextJudgment))
      }
    } yield nextJudgment #:: allFollowingJudgments

    val gameFut = for {
      judgmentStream <- judgmentsAfter(initialGuesser)
    } yield {
      val comparatives = judgmentStream.collect {
        case cj: ComparativeJudgment => cj
      }.toList
      val answer = judgmentStream.collect {
        case AnswerJudgment(answer) => answer
      }.head
      CompleteGameLog("cmd", vocabulary, comparatives, answer)
    }

    val gameLog = Await.result(gameFut, Duration.Inf)
    println(gameLog)
  }
}

object Game {
  // example games
  def makeCommandLineRandomGuessingGame(vocab: Set[String]) = {
    val input = new java.util.Scanner(System.in)
    new Game {
      override val vocabulary = vocab
      override val initialGuesser =
        RandomNonRepeatingGuesser(vocabulary, Set.empty[String])
      override def makePlayer()(implicit ec: ExecutionContext) =
        CommandLinePlayer(vocabulary, input)
      override def initialize = {
        println(s"choose a word from the following:\n${vocab.mkString(" ")}\n(you don't have to type it. Just decide and press enter.)")
        input.nextLine()

      }
    }
  }
}
