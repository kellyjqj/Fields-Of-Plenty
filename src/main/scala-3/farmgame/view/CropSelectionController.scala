package farmgame.view

import javafx.event.ActionEvent
import javafx.fxml.FXML
import javafx.scene.control.Button
import scalafx.stage.Stage

@FXML
class CropSelectionController():
  //model property
  @FXML private var riceButton: javafx.scene.control.Button = _
  @FXML private var soyButton: javafx.scene.control.Button = _
  @FXML private var potatoButton: javafx.scene.control.Button = _
  @FXML private var tomatoButton: javafx.scene.control.Button = _
  @FXML private var orangeButton: javafx.scene.control.Button = _

  //stage property
  var stage: Option[Stage] = None

  var selectedCrop: Option[String] = None

  //return property
  var okClicked: Boolean = false
  

  @FXML
  def handleCropChoice(event: ActionEvent): Unit =
    val btn = event.getSource.asInstanceOf[Button]
    selectedCrop = Some(btn.getText) // store crop name
    okClicked = true
    stage.foreach(_.close())

