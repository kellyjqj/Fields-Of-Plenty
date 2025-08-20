package farmgame.view

import javafx.event.ActionEvent
import javafx.fxml.FXML
import scalafx.stage.Stage

@FXML
class SelectVillagerController():
  //stage property
  var stage: Option[Stage] = None
  //return property
  var okClicked = false

  def handleOk(action: ActionEvent): Unit = {
    var okClicked = true
    stage.foreach(_.close())
  }
