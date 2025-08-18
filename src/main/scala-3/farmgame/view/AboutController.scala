package farmgame.view

import javafx.event.ActionEvent
import javafx.fxml.FXML
import scalafx.stage.Stage

//pop up
//model property (dont need because dont display any data)
//stage property
//return property

@FXML
class AboutController():
  //stage property
  var stage: Option[Stage] = None
  //return property
  var okClicked = false

  @FXML
  def handleClose(action: ActionEvent): Unit =
    okClicked = true
    stage.foreach(_.close()) //(x => x.close())


