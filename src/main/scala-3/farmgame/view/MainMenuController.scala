package farmgame.view

import farmgame.FieldsOfPlenty
import javafx.event.ActionEvent
import javafx.fxml.FXML

@FXML
class MainMenuController():
  @FXML
  def handleStart(action: ActionEvent): Unit = {
    //call main window
    println("Start game clicked")
    FieldsOfPlenty.showFarm()
  }

  @FXML
  def handleHowToPlay(action: ActionEvent): Unit =
    FieldsOfPlenty.showHowToPlay()

  @FXML
  def handleAbout(action: ActionEvent): Unit =
    FieldsOfPlenty.showAbout()

  @FXML
  def handleExit(action: ActionEvent): Unit =
    System.exit(0)
