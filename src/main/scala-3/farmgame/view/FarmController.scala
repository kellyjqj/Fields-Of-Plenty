package farmgame.view

import farmgame.model.*
import javafx.event.ActionEvent
import javafx.fxml.FXML
import javafx.scene.control.MenuItem


@FXML
class FarmController:
  @FXML
  private var villager1Label: javafx.scene.control.Label = _
  @FXML
  private var villager2Label: javafx.scene.control.Label = _
  @FXML
  private var villager1Bar: javafx.scene.control.ProgressBar = _
  @FXML
  private var villager2Bar: javafx.scene.control.ProgressBar = _
  @FXML
  private var nextDayButton: javafx.scene.control.Button = _
  @FXML
  private var farmGrid: javafx.scene.layout.GridPane = _
  @FXML
  private var cropMenu: javafx.scene.control.MenuButton = _
  @FXML
  private var villagerMenu: javafx.scene.control.MenuButton = _
  @FXML
  private var farm: farmgame.model.Farm = _
  @FXML
  private var daysSurvivedLabel: javafx.scene.control.Label = _


  @FXML
  def setFarm(f: Farm): Unit =
    farm = f
    setupCropMenu()
    setupVillagerMenu()
    renderFarm()

  @FXML def handleNextDay(event: ActionEvent): Unit =
    farm.nextDay()
    farm.people.foreach(_.nextDay())
    renderFarm()
    setupVillagerMenu()
    checkGameOver()

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
    val aliveVillagers = farm.people.filter(_.isAlive)
    for v <- aliveVillagers do
      val item = new MenuItem(v.name)
      item.setOnAction(_ => {
        villagerMenu.setText(v.name)
        println(s"Selected villager: ${v.name}")
      })
      villagerMenu.getItems.add(item)

    if aliveVillagers.isEmpty then
      villagerMenu.setText("No villagers alive")
    else
      villagerMenu.setText("Select Villager")
//    for name <- villagers do
//      val item = new MenuItem(name)
//      item.setOnAction(_ => {
//        villagerMenu.setText(name)
//        println(s"Selected villager: $name")
//      })
//      villagerMenu.getItems.add(item)
//    villagerMenu.setText("Select Villager")
//
  @FXML
  def renderFarm(): Unit =
    println("Rendering farm...")
    farmGrid.getChildren.clear()

    //update villagers
    val v1 = farm.people(0)
    val v2 = farm.people(1)

    villager1Label.setText(
      if v1.isAlive then
        s"${v1.name}: ${v1.nutritionLevel}"
      else s"${v1.name}: DEAD")
    villager2Label.setText(
      if v2.isAlive then
        s"${v2.name}: ${v2.nutritionLevel}"
      else s"${v2.name}: DEAD")

    updateProgressBar(villager1Bar, v1.nutritionLevel, v1.isAlive)
    updateProgressBar(villager2Bar, v2.nutritionLevel, v2.isAlive)

    daysSurvivedLabel.setText(s"Days Survived: ${farm.daysSurvived}")

//    villager1Bar.setProgress(v1.nutritionLevel / 100.0)
//    villager2Bar.setProgress(v2.nutritionLevel / 100.0)

    //create buttons in grid
    farmGrid.getColumnConstraints.clear()
    farmGrid.getRowConstraints.clear()

    //equal width columns and equal height rows
    for (_ <- 0 until farm.numCols) {
      val col = new javafx.scene.layout.ColumnConstraints()
      col.setPercentWidth(100.0 / farm.numCols) // each column gets equal width
      farmGrid.getColumnConstraints.add(col)
    }
    for (_ <- 0 until farm.numRows) {
      val row = new javafx.scene.layout.RowConstraints()
      row.setPercentHeight(100.0 / farm.numRows) // each row gets equal height
      farmGrid.getRowConstraints.add(row)
    }


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
          btn.setText(s"${plot.getCropName}")
          //(${plot.progress}/${plot.growthTime}
          btn.setDisable(true)

        btn.setPrefSize(Double.MaxValue, Double.MaxValue)
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
//    else if plot.isReady then
//      val harvested = farm.harvestAt(row, col)
//      villagerMenu.getText match
//        case "Alice" => harvested.foreach(crop => farm.feedVillager(0, crop))
//        case "Bob" => harvested.foreach(crop => farm.feedVillager(1, crop))
//        case _ => println("No villager selected")
    else if plot.isReady then
      val selected = villagerMenu.getText
      if selected == "Select Villager" || selected == "No villagers alive" then
        println("⚠ Please select a living villager before harvesting!")
        return
      else
        val index = farm.people.indexWhere(_.name == selected)
        if index < 0 then
          println(s"⚠ Villager $selected not found!")
          return
        val villager = farm.people(index)
        if !villager.isAlive then
          println(s"⚠ Cannot feed dead villager ${villager.name}!")
          return
        val harvested = farm.harvestAt(row, col)
        harvested.foreach { crop =>
          farm.feedVillager(index, crop)
        }
//    else if plot.isReady then
//      val selected = villagerMenu.getText
//      if selected == "Select Villager" || selected == "No villagers alive" then
//        println("⚠ Please select a living villager before harvesting!")
//        return
//      else
//        val harvested = farm.harvestAt(row, col)
//        harvested.foreach { crop =>
//          val index = farm.people.indexWhere(_.name == selected)
//          if index >= 0 then farm.feedVillager(index, crop)
//        }

    renderFarm()

  private def updateProgressBar(bar: javafx.scene.control.ProgressBar, nutrition: Int, alive: Boolean): Unit =
    val fraction = nutrition / 100.0
    bar.setProgress(fraction)

    // clear old classes
    bar.getStyleClass.removeAll("nutrition-dead", "nutrition-low", "nutrition-medium", "nutrition-high")

    // add new class depending on state
    if !alive then
      bar.getStyleClass.add("nutrition-dead")
    else if nutrition < 30 then
      bar.getStyleClass.add("nutrition-low")
    else if nutrition < 60 then
      bar.getStyleClass.add("nutrition-medium")
    else
      bar.getStyleClass.add("nutrition-high")


  private def checkGameOver(): Unit =
    if farm.people.forall(!_.isAlive) then {
      println(s"DEBUG: Game Over at day ${farm.daysSurvived}")
      val days = farm.daysSurvived
      farmgame.FieldsOfPlenty.showGameOver(days)
    }

