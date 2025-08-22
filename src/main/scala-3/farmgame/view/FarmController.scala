package farmgame.view

import farmgame.FieldsOfPlenty
import farmgame.model.*
import javafx.event.ActionEvent
import javafx.fxml.FXML
import javafx.scene.control.MenuItem
import javafx.scene.image.Image


@FXML
class FarmController:
  @FXML
  private var villager1Image: javafx.scene.image.ImageView = _
  @FXML
  private var villager2Image: javafx.scene.image.ImageView = _
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

  //to keep track of which villager is eating
  private var currentlyEating: Set[Int] = Set()

//villager sprites
  private val v1Normal = new Image(getClass.getResourceAsStream("/farmgame/view/images/v1.png"))
  private val v1Happy = new Image(getClass.getResourceAsStream("/farmgame/view/images/v1happy.png"))
  private val v1Sad = new Image(getClass.getResourceAsStream("/farmgame/view/images/v1sad.png"))
  private val v1Eating = new Image(getClass.getResourceAsStream("/farmgame/view/images/v1eat.png"))
  private val v1Dead = new Image(getClass.getResourceAsStream("/farmgame/view/images/v1dead.png"))

  private val v2Normal = new Image(getClass.getResourceAsStream("/farmgame/view/images/v2.png"))
  private val v2Happy = new Image(getClass.getResourceAsStream("/farmgame/view/images/v2happy.png"))
  private val v2Sad = new Image(getClass.getResourceAsStream("/farmgame/view/images/v2sad.png"))
  private val v2Eating = new Image(getClass.getResourceAsStream("/farmgame/view/images/v2eat.png"))
  private val v2Dead = new Image(getClass.getResourceAsStream("/farmgame/view/images/v2dead.png"))

  @FXML
  def setFarm(f: Farm): Unit =
    farm = f
    setupCropMenu()
    setupVillagerMenu()
    renderFarm()

  @FXML
  def handleNextDay(event: ActionEvent): Unit =
    farm.nextDay()
    farm.people.foreach(_.nextDay())
    renderFarm()
    setupVillagerMenu()
    checkGameOver()

  private def renderFarm(): Unit =
    println("Rendering farm...")
    farmGrid.getChildren.clear()

    val v1 = farm.people.head
    val v2 = farm.people(1)

    updateVillagerUI(0, v1, villager1Label, villager1Image, villager1Bar)
    updateVillagerUI(1, v2, villager2Label, villager2Image, villager2Bar)

    daysSurvivedLabel.setText(s"Days Survived: ${farm.daysSurvived}")

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
          
//          //drag n drop
//          btn.setOnDragDetected{ event => 
//            val db = btn.startDragAndDrop(javafx.scene.input.TransferMode.MOVE)
//            val content = new javafx.scene.input.ClipboardContent()
//            content.putString(s"$row, $col") //encode which plot
//            db.setContent(content)
//            event.consume()
//          }
//          


        else
          btn.setText(s"${plot.getCropName}")
          btn.setDisable(true)

        btn.setPrefSize(Double.MaxValue, Double.MaxValue)
        farmGrid.add(btn, col, row)

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

  private def updateVillagerUI(index: Int,
                                villager: Villager,
                                label: javafx.scene.control.Label,
                                imageView: javafx.scene.image.ImageView,
                                progressBar: javafx.scene.control.ProgressBar
                              ): Unit = {
    val fraction = villager.nutritionLevel / 100.0
    progressBar.setProgress(fraction)

    val nutritionClasses = List("nutrition-dead", "nutrition-low", "nutrition-medium", "nutrition-high")
    progressBar.getStyleClass.removeAll(nutritionClasses: _*)

    if villager.isAlive then
      label.setText(s"${villager.name}: ${villager.nutritionLevel}")

      //set villager sprite
      if currentlyEating.contains(index) then
        imageView.setImage(if index == 0 then v1Eating else v2Eating)
      else if villager.nutritionLevel < 30 then
        imageView.setImage(if index == 0 then v1Sad else v2Sad)
      else if villager.nutritionLevel < 60 then
        imageView.setImage(if index == 0 then v1Normal else v2Normal)
      else
        imageView.setImage(if index == 0 then v1Happy else v2Happy)

    else
      label.setText(s"${villager.name}: DEAD")
      imageView.setImage(if index == 0 then v1Dead else v2Dead)

    //progressbar
    if !villager.isAlive then
      progressBar.getStyleClass.add("nutrition-dead")
    else if villager.nutritionLevel < 30 then
      progressBar.getStyleClass.add("nutrition-low")
    else if villager.nutritionLevel < 60 then
      progressBar.getStyleClass.add("nutrition-medium")
    else
      progressBar.getStyleClass.add("nutrition-high")
  }

  private def playEatingAnimation(villagerIndex: Int): Unit =
    currentlyEating += villagerIndex

    val (imageView, eatingImg, normalImg) =
      if villagerIndex == 0 then
        (villager1Image, v1Eating, v1Normal)
      else (villager2Image, v2Eating, v2Normal)

    imageView.setImage(eatingImg)

    val pause = new javafx.animation.PauseTransition(javafx.util.Duration.seconds(1))
    pause.setOnFinished(_ => {
      currentlyEating -= villagerIndex
      renderFarm()
    })
    pause.play()

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
          FieldsOfPlenty.showSelectCrop()

    else if plot.isReady then
      val selected = villagerMenu.getText
      if selected == "Select Villager" || selected == "No villagers alive" then
        println("⚠ Please select a living villager before harvesting!")
        FieldsOfPlenty.showSelectVillager()
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
          playEatingAnimation(index)
        }

    renderFarm()

  private def checkGameOver(): Unit = {
    if farm.people.forall(!_.isAlive) then {
      println(s"DEBUG: Game Over at day ${farm.daysSurvived}")
      val days = farm.daysSurvived
      farmgame.FieldsOfPlenty.showGameOver(days)
    }
    if farm.people.forall(_.isAlive) && farm.people.forall(_.nutritionLevel >= 100) then {
      //farmgame.FieldsOfPlenty.showGameWon, show days Survived
    }
  }

