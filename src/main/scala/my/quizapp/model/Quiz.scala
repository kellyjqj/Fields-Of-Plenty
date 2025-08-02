package my.quizapp.model

import my.quizapp.util.Category

class Quiz(val title: String, 
           val questions: List[Question], 
           val category: Category):
  def getNextQuestion(): Question = ???
  