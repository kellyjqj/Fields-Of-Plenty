package farmgame.view

import javafx.event.ActionEvent
import javafx.fxml.FXML
import farmgame.FieldsOfPlenty

@FXML
class RootLayoutController():
  @FXML
  def handleClose(action:ActionEvent): Unit =
    System.exit(0)

  @FXML
  def handleDelete(action:ActionEvent): Unit = {

  }

  @FXML
  def handleAbout(action: ActionEvent): Unit =
    FieldsOfPlenty.showAbout()


