package farmgame.view

import farmgame.FieldsOfPlenty
import farmgame.model.*
import javafx.event.ActionEvent
import javafx.fxml.FXML
import javafx.scene.layout.GridPane
import javafx.scene.control.{Button, Label, MenuButton, MenuItem}
import scalafx.event
import scalafx.stage.Stage


@FXML
class FarmController:
  @FXML
  private var villager1Label: javafx.scene.control.Label = _
  @FXML
  private var villager2Label: javafx.scene.control.Label = _
  @FXML
  private var nextTurnButton: javafx.scene.control.Button = _

  @FXML
  private var plot00: javafx.scene.control.Button = _
  @FXML
  private var plot01: javafx.scene.control.Button = _
  @FXML
  private var plot02: javafx.scene.control.Button = _
  @FXML
  private var plot10: javafx.scene.control.Button = _
  @FXML
  private var plot11: javafx.scene.control.Button = _
  @FXML
  private var plot12: javafx.scene.control.Button = _
  @FXML
  private var plot20: javafx.scene.control.Button = _
  @FXML
  private var plot21: javafx.scene.control.Button = _
  @FXML
  private var plot22: javafx.scene.control.Button = _

  @FXML
  private var farm: farmgame.model.Farm = _

  @FXML
  def setFarm(f: Farm): Unit =
    farm = f
    renderFarm()

  @FXML def initialize(): Unit =
    nextTurnButton.setOnAction(_ => {
      farm.nextTurn()
      renderFarm()
    })

  @FXML
  def handlePlotAction(action: ActionEvent): Unit =
    val btn = action.getSource.asInstanceOf[javafx.scene.control.Button]
    println(s"Button pressed: ${btn.getId}")
    btn.getId match
      case "plot00" => handlePlot(0, 0)
      case "plot01" => handlePlot(0, 1)
      case "plot02" => handlePlot(0, 2)
      case "plot10" => handlePlot(1, 0)
      case "plot11" => handlePlot(1, 1)
      case "plot12" => handlePlot(1, 2)
      case "plot20" => handlePlot(2, 0)
      case "plot21" => handlePlot(2, 1)
      case "plot22" => handlePlot(2, 2)

  @FXML
  private def handlePlot(row: Int, col: Int): Unit =
    val plot = farm.plots(row)(col)
    if plot.isEmpty then
      println("Showing crop selection popup...")
      val ownerStage = nextTurnButton.getScene.getWindow.asInstanceOf[Stage]
      println("Calling showCropSelection")
      val selectedCrop = FieldsOfPlenty.showCropSelection(ownerStage)
      println(s"Crop selected: $selectedCrop")
      selectedCrop.foreach {
        case "Rice" => farm.plantAt(row, col, new Rice)
        case "Soy" => farm.plantAt(row, col, new Soy)
        case "Potato" => farm.plantAt(row, col, new Potato)
        case "Tomato" => farm.plantAt(row, col, new Tomato)
        case "Orange" => farm.plantAt(row, col, new Orange)
        case _ =>
      }
      renderFarm()

    else if plot.isReady then
      val harvested = farm.harvestAt(row, col)
      harvested.foreach(crop => farm.feedVillager(0, crop)) // villager selection later
      renderFarm()
  @FXML
  private def renderFarm(): Unit =
    // update villagers
    villager1Label.setText(s"${farm.people(0).name}: ${farm.people(0).nutritionLevel}")
    villager2Label.setText(s"${farm.people(1).name}: ${farm.people(1).nutritionLevel}")

    // update buttons based on plot state
    updateButton(plot00, farm.plots(0)(0))
    updateButton(plot01, farm.plots(0)(1))
    updateButton(plot02, farm.plots(0)(2))
    updateButton(plot10, farm.plots(1)(0))
    updateButton(plot11, farm.plots(1)(1))
    updateButton(plot12, farm.plots(1)(2))
    updateButton(plot20, farm.plots(2)(0))
    updateButton(plot21, farm.plots(2)(1))
    updateButton(plot22, farm.plots(2)(2))

  @FXML
  private def updateButton(btn: Button, plot: FarmPlot): Unit =
    if plot.isEmpty then
      btn.setText("Plant")
      btn.setDisable(false)
    else if plot.isReady then
      btn.setText(s"Harvest ${plot.getCropName}")
      btn.setDisable(false)
    else
      btn.setText(s"${plot.getCropName} (${plot.progress}/${plot.growthTime})")
      btn.setDisable(true)
