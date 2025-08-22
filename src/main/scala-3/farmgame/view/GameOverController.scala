package farmgame.view

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
  private var villager1GraveImage: javafx.scene.image.ImageView = _
  @FXML
  private var villager2GraveImage: javafx.scene.image.ImageView = _

  private val v1grave = new javafx.scene.image.Image(getClass.getResourceAsStream("/farmgame/view/images/v1grave.png"))
  private val v2grave = new javafx.scene.image.Image(getClass.getResourceAsStream("/farmgame/view/images/v2grave.png"))

  def setScore(days: Int, win: Boolean): Unit =
    println(s"DEBUG: setScore called with $days")
    finalDaysSurvivedLabel.setText(s"Days Survived: $days")

    if gameOverLabel != null then
      gameOverLabel.setText(
        if win then "Congratulations!!!"
        else "Game Over!!!"
      )
    if gameOverDescLabel != null then
      gameOverDescLabel.setText(
        if win then "All villagers are happy and healthy :D"
        else "All villagers have died :("
      )
    if !win then
      villager1GraveImage.setImage(v1grave)
      villager2GraveImage.setImage(v2grave)



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


