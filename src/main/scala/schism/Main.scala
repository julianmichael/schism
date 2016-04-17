package schism

import scala.concurrent._

object Main extends App {
  val vocab = Set("apple", "banana", "cucumber", "durian", "enchilada")
  val cmdLineGame = Game.makeCommandLineRandomGuessingGame(vocab)
  implicit val ec: ExecutionContext = ExecutionContext.global
  cmdLineGame.play()
}
