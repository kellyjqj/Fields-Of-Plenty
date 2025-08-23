package farmgame.view

import javafx.event.ActionEvent
import javafx.fxml.FXML
import farmgame.FieldsOfPlenty
import farmgame.model.VillagerAssets

@FXML
class RootLayoutController():
  @FXML
  def handleClose(action:ActionEvent): Unit =
    System.exit(0)

  @FXML
  def handleMainMenu(action: ActionEvent): Unit =
    FieldsOfPlenty.showMainMenu()

  @FXML
  def handleRestartGame(action: ActionEvent): Unit =
    FieldsOfPlenty.showFarm()

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
      farmgame.model.Villager("Alice", VillagerAssets.v1Sprites),
      farmgame.model.Villager("Bob", VillagerAssets.v2Sprites),
    ))
    farmgame.FieldsOfPlenty.showFarm(newFarm)

  @FXML
  def handleHowToPlay(): Unit =
    FieldsOfPlenty.showHowToPlay()

  @FXML
  def handleCropInfo(): Unit =
    FieldsOfPlenty.showCropInfo()