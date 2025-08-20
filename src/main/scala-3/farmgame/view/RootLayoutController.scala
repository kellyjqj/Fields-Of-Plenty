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
  def handleAbout(action: ActionEvent): Unit =
    FieldsOfPlenty.showAbout()

  @FXML
  def handleFarmSize3(action: ActionEvent): Unit =
    restartFarm(3, 3)

  @FXML
  def handleFarmSize4(action: ActionEvent): Unit =
    restartFarm(4, 4)

  @FXML
  def handleFarmSize5(action: ActionEvent): Unit =
    restartFarm(5, 5)

  private def restartFarm(rows: Int, cols: Int): Unit =
    println(s"Restarting farm with size ${rows}x${cols}")
    val newFarm = farmgame.model.Farm(rows, cols, List(
      farmgame.model.Villager("Alice"), farmgame.model.Villager("Bob")
    ))
    farmgame.FieldsOfPlenty.showFarm(newFarm)

  @FXML
  def handleHowToPlay(): Unit =
    FieldsOfPlenty.showHowToPlay()

  @FXML
  def handleCropInfo(): Unit =
    FieldsOfPlenty.showCropInfo()