package my.quizapp.model

class User(val username: String,
           val score: Int,
           val history: List[Result]):
  def updateScore(points: Int): Unit = ???

  def addHistory(result: Result): Unit = ???









