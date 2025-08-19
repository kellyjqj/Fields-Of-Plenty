package farmgame.view

import javafx.event.ActionEvent
import javafx.fxml.FXML
import scalafx.beans.property.IntegerProperty
import scalafx.stage.Stage


@FXML
class GameOverController:
  var stage: Option[Stage] = None
  var okClicked = false

  @FXML
  private var finalDaysSurvivedLabel: javafx.scene.control.Label = _

  def setScore(days: Int): Unit =
    println(s"DEBUG: setScore called with $days")
    finalDaysSurvivedLabel.setText(s"Days Survived: $days")

  @FXML
  def handleRestart(action: ActionEvent): Unit = {
    println("Restarting game...")
    farmgame.FieldsOfPlenty.showFarm()
    okClicked = true
    stage.foreach(_.close())
  }

  @FXML
  def handleQuit(action: ActionEvent): Unit = {
    farmgame.FieldsOfPlenty.showMainMenu()
    okClicked = true
    stage.foreach(_.close())
  }


