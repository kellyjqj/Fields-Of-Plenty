package farmgame.view

import farmgame.FieldsOfPlenty
import farmgame.model.*
import javafx.event.ActionEvent
import javafx.fxml.{FXML, FXMLLoader}
import javafx.scene.control.MenuItem


@FXML
class FarmController:
  @FXML
  private var villager1Label: javafx.scene.control.Label = _
  @FXML
  private var villager2Label: javafx.scene.control.Label = _
  @FXML
  private var nextTurnButton: javafx.scene.control.Button = _
  @FXML
    //should be empty in fxml
  private var farmGrid: javafx.scene.layout.GridPane = _

  @FXML
  private var cropMenu: javafx.scene.control.MenuButton = _

  @FXML
  private var villagerMenu: javafx.scene.control.MenuButton = _

  @FXML
  private var farm: farmgame.model.Farm = _

  @FXML
  def setFarm(f: Farm): Unit =
    farm = f
    setupCropMenu()
    setupVillagerMenu()
    renderFarm()

  @FXML def handleNextTurn(event: ActionEvent): Unit =
    farm.nextTurn()
    renderFarm()

  @FXML
  def setupCropMenu(): Unit =
    val crops = List("Rice", "Soy","Potato", "Tomato", "Orange")
    cropMenu.getItems.clear()
    for crop <- crops do
      val item = new MenuItem(crop)
      item.setOnAction(_ => {
        cropMenu.setText(crop)
        println(s"Selected crop: $crop")
      })
      cropMenu.getItems.add(item)
    cropMenu.setText("Select Crop")

  @FXML
  def setupVillagerMenu(): Unit =
    val villagers = farm.people.map(_.name)

    villagerMenu.getItems.clear()
    for name <- villagers do
      val item = new MenuItem(name)
      item.setOnAction(_ => {
        villagerMenu.setText(name)
        println(s"Selected villager: $name")
      })
      villagerMenu.getItems.add(item)
    villagerMenu.setText("Select Villager")

  @FXML
  def renderFarm(): Unit =
    println("Rendering farm...")
    farmGrid.getChildren.clear()

    //update villagers
    villager1Label.setText(s"${farm.people(0).name}: ${farm.people(0).nutritionLevel}")
    villager2Label.setText(s"${farm.people(1).name}: ${farm.people(1).nutritionLevel}")

    //create buttons in grid
    for row <- 0 until farm.numRows do
      for col <- 0 until farm.numCols do
        val plot = farm.plots(row)(col)
        val btn = new javafx.scene.control.Button()

        if plot.isEmpty then
          btn.setText("Plant")
          btn.setDisable(false)
          btn.setOnAction(_ => handlePlot(row, col))
        else if plot.isReady then
          btn.setText(s"Harvest ${plot.getCropName}")
          btn.setDisable(false)
          btn.setOnAction(_ => handlePlot(row, col))
        else
          btn.setText(s"${plot.getCropName} (${plot.progress}/${plot.growthTime})")
          btn.setDisable(true)

        btn.setPrefSize(100, 50)
        farmGrid.add(btn, col, row)

  @FXML
  def handlePlot(row: Int, col: Int): Unit =
    val plot = farm.plots(row)(col)

    if plot.isEmpty then
      cropMenu.getText match
        case "Rice" => farm.plantAt(row, col, new Rice)
        case "Soy" => farm.plantAt(row, col, new Soy)
        case "Potato" => farm.plantAt(row, col, new Potato)
        case "Tomato" => farm.plantAt(row, col, new Tomato)
        case "Orange" => farm.plantAt(row, col, new Orange)
        case _ => println("No crop selected")
    else if plot.isReady then
      val harvested = farm.harvestAt(row, col)
      villagerMenu.getText match
        case "Alice" => harvested.foreach(crop => farm.feedVillager(0, crop))
        case "Bob" => harvested.foreach(crop => farm.feedVillager(1, crop))
        case _ => println("No villager selected")

    renderFarm()


