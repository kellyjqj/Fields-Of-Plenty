package my.quizapp.view

import my.quizapp.MainQuizApp
import javafx.event.ActionEvent
import javafx.fxml.FXML

@FXML
class RootLayoutController:
  @FXML
  def handleClose(action: ActionEvent): Unit =
    MainQuizApp.stage.close()