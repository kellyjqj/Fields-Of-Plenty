package farmgame.view

import javafx.event.ActionEvent
import javafx.fxml.FXML
import scalafx.stage.Stage

@FXML
class WarningDialogController():
  //stage property
  var stage: Option[Stage] = None
  //return property
  var okClicked = false

  @FXML
  private var warningLabel1: javafx.scene.control.Label = _
  @FXML
  private var warningLabel2: javafx.scene.control.Label = _

  def setDialogStage(stage: Stage): Unit =
    this.stage = Some(stage)

  def setMessage(message1: String, message2: String): Unit = {
    warningLabel1.setText(message1)
    warningLabel2.setText(message2)
  }

  def handleOk(action: ActionEvent): Unit = {
    var okClicked = true
    stage.foreach(_.close())
  }
