package my.quizapp.model

import my.quizapp.util.Category

abstract class Question(val category: Category, val prompt: String):
  def checkAnswer(answer: String): Boolean = ???
  def checkAnswer(answer: Map[String, String]): Boolean = ???

//===============================inheritance========================================

class TrueFalse(category: Category, prompt: String, val correct: Boolean)
  extends Question(category, prompt):

  override def checkAnswer(answer: String): Boolean = ???

class MCQ(category: Category, prompt: String, options: List[String], ans: String)
  extends Question(category, prompt):

  override def checkAnswer(answer: String): Boolean = ???

class FillBlanks(category: Category, prompt: String, ans: Map[String, String])
  extends Question(category, prompt):
  
  override def checkAnswer(answer: Map[String, String]): Boolean = ???
