package my.quizapp.view

import javafx.fxml.FXML
import javafx.event.ActionEvent
import my.quizapp.MainQuizApp

@FXML
class MainMenuController:

  @FXML
  def handlePlay(event: ActionEvent): Unit =
    println("Play clicked")
    //MainQuizApp.loadCategorySelection
  // e.g., MainQuizApp.loadQuizView()

  @FXML
  def handleResult(event: ActionEvent): Unit =
    println("Result clicked")
  // e.g., MainQuizApp.showResultView()

  @FXML
  def handleQuit(event: ActionEvent): Unit =
    MainQuizApp.stage.close()