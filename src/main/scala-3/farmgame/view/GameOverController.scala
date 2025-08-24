package farmgame.view

import farmgame.model.VillagerAssets
import javafx.event.ActionEvent
import javafx.fxml.FXML
import scalafx.beans.property.IntegerProperty
import scalafx.stage.Stage

import scala.runtime.Nothing$


@FXML
class GameOverController:
  var stage: Option[Stage] = None
  var okClicked = false

  @FXML
  private var gameOverLabel: javafx.scene.control.Label = _
  @FXML
  private var gameOverDescLabel: javafx.scene.control.Label = _
  @FXML
  private var finalDaysSurvivedLabel: javafx.scene.control.Label = _
  @FXML
  private var villager1EndImage: javafx.scene.image.ImageView = _
  @FXML
  private var villager2EndImage: javafx.scene.image.ImageView = _
  
  def setScore(days: Int, win: Boolean): Unit =
    println(s"DEBUG: setScore called with $days")
    finalDaysSurvivedLabel.setText(s"Days Survived: $days")

    //set game over text and image
    if gameOverLabel != null then
      gameOverLabel.setText(
        if win then {
          gameOverLabel.getStyleClass.add("game-over-win")
          finalDaysSurvivedLabel.getStyleClass.add("game-over-win")
          "Congratulations!!!"
        } else {
          gameOverLabel.getStyleClass.add("game-over-lose")
          finalDaysSurvivedLabel.getStyleClass.add("game-over-lose")
          "Game Over!!!"
        }
      )
    if gameOverDescLabel != null then
      gameOverDescLabel.setText(
        if win then {
          gameOverDescLabel.getStyleClass.add("game-over-win")
          finalDaysSurvivedLabel.getStyleClass.add("game-over-win")
          "All villagers are happy and healthy :D"
        } else {
          gameOverDescLabel.getStyleClass.add("game-over-lose")
          finalDaysSurvivedLabel.getStyleClass.add("game-over-lose")
          "All villagers have died :("
        }
      )
    if !win then
      villager1EndImage.setImage(VillagerAssets.grave1)
      villager2EndImage.setImage(VillagerAssets.grave2)
    else
      villager1EndImage.setImage(VillagerAssets.v1Sprites.happy)
      villager2EndImage.setImage(VillagerAssets.v2Sprites.happy)

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


