package farmgame.view

import farmgame.FieldsOfPlenty
import farmgame.model.*
import javafx.event.ActionEvent
import javafx.fxml.FXML
import javafx.scene.control.MenuItem
import javafx.scene.image.Image
import javafx.animation.{KeyFrame, Timeline}
import javafx.util.Duration

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
  private val v1Sprites: VillagerSprites = VillagerAssets.v1Sprites
  private val v2Sprites: VillagerSprites = VillagerAssets.v2Sprites


  val v1 = new Villager("Alice", v1Sprites)
  val v2 = new Villager("Bob", v2Sprites)

  @FXML
  def setFarm(f: Farm): Unit =
    farm = f
    setupCropMenu()
    setupVillagerMenu()
    setupDragAndDrop()
    renderFarm()

  @FXML
  def handleNextDay(event: ActionEvent): Unit =
    farm.nextDay()
    farm.people.foreach(_.nextDay())
    renderFarm()
    setupCropMenu()
    setupVillagerMenu()
    checkGameOver()

  private def renderFarm(): Unit =
    println("Rendering farm...")
    farmGrid.getChildren.clear()

    val v1 = farm.people.head
    val v2 = farm.people(1)

    updateVillagerUI(v1, villager1Label, villager1Image, villager1Bar)
    updateVillagerUI(v2, villager2Label, villager2Image, villager2Bar)

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

          //drag n drop
          btn.setOnDragDetected{ event =>
            val db = btn.startDragAndDrop(javafx.scene.input.TransferMode.MOVE)
            val content = new javafx.scene.input.ClipboardContent()
            content.putString(s"$row,$col") //encode which plot
            db.setContent(content)
            event.consume()
          }
          
        else
          btn.setText(s"${plot.getCropName}")
          btn.setDisable(true)

        btn.setMaxSize(Double.MaxValue, Double.MaxValue)
        farmGrid.add(btn, col, row)
  
  private def setupCropMenu(): Unit =
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

  private def setupVillagerMenu(): Unit =
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

  private def updateVillagerUI(villager: Villager,
                                label: javafx.scene.control.Label,
                                imageView: javafx.scene.image.ImageView,
                                progressBar: javafx.scene.control.ProgressBar
                              ): Unit = {
    val fraction = villager.nutritionLevel / 100.0
    progressBar.setProgress(fraction)

    val nutritionClasses = List("nutrition-dead", "nutrition-low", "nutrition-medium", "nutrition-high")
    progressBar.getStyleClass.removeAll(nutritionClasses: _*)

    val mood = villager.currentMood
    if mood.isAnimated then
      playEatingAnimation(imageView, mood.frames(villager.sprites), villager)
    else
      imageView.setImage(mood.sprite(villager.sprites))

    label.setText(
      if villager.isAlive then s"${villager.name}: ${villager.nutritionLevel.toInt}"
      else s"${villager.name}: DEAD")

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

  private def playEatingAnimation(imageView: javafx.scene.image.ImageView,
                                  frames: Seq[Image],
                                  villager: Villager
                                 ): Unit = {
    villager.isEating = true
    val timeline = new javafx.animation.Timeline()
    for (i <- frames.indices) {
      timeline.getKeyFrames.add(
        new javafx.animation.KeyFrame(
          javafx.util.Duration.millis(150 * i),
          _ => imageView.setImage(frames(i))
        )
      )
    }
    timeline.getKeyFrames.add(
      new javafx.animation.KeyFrame(
        javafx.util.Duration.millis(150 * frames.size),
        _ => {
          villager.isEating = false
          renderFarm() // refresh after done
        }
      )
    )
    timeline.play()
  }
    
  private def setupDragAndDrop(): Unit = 
    setupVillagerDrop(villager1Image, farm.people.head)
    setupVillagerDrop(villager2Image, farm.people(1))
    
  private def setupVillagerDrop(imageView: javafx.scene.image.ImageView,
                                villager: Villager
                               ): Unit = {
    imageView.setOnDragOver { event => 
      if event.getGestureSource != imageView && event.getDragboard.hasString then
        event.acceptTransferModes(javafx.scene.input.TransferMode.MOVE)
      event.consume()
    }
    imageView.setOnDragDropped { event =>
      val db = event.getDragboard
      if db.hasString then
        val parts = db.getString.split(",")
        val row = parts(0).toInt
        val col = parts(1).toInt

        val harvested = farm.harvestAt(row, col)
        harvested.foreach { crop =>
          farm.feedVillager(villager, crop) // feed by villager instance
          playEatingAnimation(imageView, villager.sprites.eatingFrames, villager)
        }

        checkGameOver()
        renderFarm()
        event.setDropCompleted(true)
      else event.setDropCompleted(false)
      event.consume()
    }

    imageView.setOnDragEntered(_ =>
      imageView.setImage(villager.sprites.eatingFrames.head))
    imageView.setOnDragExited(_ =>
      renderFarm())
  }

  private def handlePlot(row: Int, col: Int): Unit =
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
          farm.feedVillager(villager, crop)
          val targetImageView =
            if index == 0 then villager1Image else villager2Image

          playEatingAnimation(targetImageView, villager.sprites.eatingFrames, villager)
        }
        checkGameOver()

    renderFarm()

  private def checkGameOver(): Unit = {
    if farm.people.forall(!_.isAlive) then {
      println(s"DEBUG: Game Over at day ${farm.daysSurvived}")
      val days = farm.daysSurvived
      farmgame.FieldsOfPlenty.showGameOver(days, win = false)
    }
    else if farm.people.forall(_.isAlive)
      && farm.people.forall(_.nutritionLevel >= 100) then {
      println("DEBUG: Game Won!")
      val days = farm.daysSurvived
      farmgame.FieldsOfPlenty.showGameOver(days, win = true)
    }
  }

