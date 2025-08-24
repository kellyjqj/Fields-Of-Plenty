package farmgame
import farmgame.model.{Farm, Villager, VillagerAssets}
import farmgame.view.*
import javafx.fxml.FXMLLoader
import scalafx.application.JFXApp3
import scalafx.application.JFXApp3.PrimaryStage
import scalafx.scene.Scene
import scalafx.Includes.*
import scalafx.stage.Modality.ApplicationModal
import scalafx.stage.Stage

import java.net.URL

//references: 
//https://docs.oracle.com/javase/8/javafx/events-tutorial/drag-drop.htm
//https://docs.oracle.com/javase/8/javafx/api/javafx/animation/Timeline.html#Timeline-javafx.animation.KeyFrame...-


object FieldsOfPlenty extends JFXApp3:
  var rootPane: Option[javafx.scene.layout.BorderPane] = None
  override def start(): Unit = 
    val rootLayoutResource: URL = getClass.getResource("/farmgame/view/RootLayout.fxml")
    val loader = new FXMLLoader(rootLayoutResource)
    val rootLayout = loader.load[javafx.scene.layout.BorderPane]()
    //initialize rootPane
    rootPane = Option(loader.getRoot[javafx.scene.layout.BorderPane]())
    stage = new PrimaryStage():
      title = "Fields of Plenty"
      scene = new Scene():
        root = rootLayout
        stylesheets.add(getClass.getResource("/farmgame/view/style.css").toExternalForm)

    showMainMenu()//main menu

  //show main menu page
  def showMainMenu(): Unit = 
    val mainMenu: URL = getClass.getResource("/farmgame/view/MainMenu.fxml")
    val loader = new FXMLLoader(mainMenu)
    val pane = loader.load[javafx.scene.layout.AnchorPane]()
    rootPane.foreach(_.setCenter(pane))

  //show farm page(default)
  def showFarm(): Unit =
    showFarm(createDefaultFarm())

  //show farm page with given farm
  def showFarm(farm: Farm): Unit =
    try {
      val farmResource: URL = getClass.getResource("/farmgame/view/Farm.fxml")
      val loader = new FXMLLoader(farmResource)
      val pane = loader.load[javafx.scene.layout.AnchorPane]()
      val ctrl = loader.getController[FarmController]()
      ctrl.setFarm(farm)
      rootPane.foreach(_.setCenter(pane))
    } catch {
      case e: Exception =>
        println("Error loading Farm.fxml")
        e.printStackTrace()
    }

  //set default farm
  private def createDefaultFarm(): Farm =
    Farm(3, 3, List(Villager("Alice", VillagerAssets.v1Sprites),
                    Villager("Bob", VillagerAssets.v2Sprites)))

  //game over page
  def showGameOver(days: Int, win: Boolean): Boolean =
    val gameOver = getClass.getResource("/farmgame/view/GameOver.fxml")
    val loader = new FXMLLoader(gameOver)
    loader.load()
    val pane = loader.getRoot[javafx.scene.layout.AnchorPane]()
    val myWindow = new Stage():
      initOwner(stage)
      initModality(ApplicationModal)
      title = if win then "Congratulations!" else "Game Over"
      scene = new Scene():
        root = pane
    val ctrl = loader.getController[GameOverController]()
    ctrl.setScore(days, win)

    ctrl.stage = Option(myWindow)
    myWindow.showAndWait() //popup
    ctrl.okClicked

  //popup dialogs
  def showAbout(): Boolean =
    val about = getClass.getResource("/farmgame/view/About.fxml")
    val loader = new FXMLLoader(about)
    loader.load()
    val pane = loader.getRoot[javafx.scene.layout.AnchorPane]()
    val myWindow = new Stage():
      initOwner(stage) //owner of popup dialog = primary stage
      initModality(ApplicationModal) //stayontop
      title = "About"
      scene = new Scene():
        root = pane
      //initialize controller
    val ctrl = loader.getController[AboutController]()
    ctrl.stage = Option(myWindow)
    myWindow.showAndWait() //popup
    ctrl.okClicked
  
  def showHowToPlay(): Boolean = 
    val howToPlay = getClass.getResource("/farmgame/view/HowToPlay.fxml")
    val loader = new FXMLLoader(howToPlay)
    loader.load()
    val pane = loader.getRoot[javafx.scene.layout.AnchorPane]()
    val myWindow = new Stage():
      initOwner(stage)
      initModality(ApplicationModal)
      title = "How to Play"
      scene = new Scene():
        root = pane
    val ctrl = loader.getController[HowToPlayController]()
    ctrl.stage = Option(myWindow)
    myWindow.showAndWait()
    ctrl.okClicked

  def showCropInfo(): Boolean = 
    val cropInfo = getClass.getResource("/farmgame/view/CropInfo.fxml")
    val loader = new FXMLLoader(cropInfo)
    loader.load()
    val pane = loader.getRoot[javafx.scene.layout.AnchorPane]()
    val myWindow = new Stage():
      initOwner(stage)
      initModality(ApplicationModal)
      title = "Crop Info"
      scene = new Scene():
        root = pane
    val ctrl = loader.getController[CropInfoController]()
    ctrl.stage = Option(myWindow)
    myWindow.showAndWait()
    ctrl.okClicked

///show select villager/select crop
  def showWarningDialog(message1: String, message2: String): Boolean =
    val warning = getClass.getResource("/farmgame/view/WarningDialog.fxml")
    val loader = new FXMLLoader(warning)
    loader.load
    val pane = loader.getRoot[javafx.scene.layout.AnchorPane]()
    val myWindow = new Stage(): 
      initOwner(stage)
      initModality(ApplicationModal)
      title = "Warning"
      scene = new Scene():
        root = pane
    val ctrl = loader.getController[WarningDialogController]()
    ctrl.stage = Option(myWindow)
    ctrl.setMessage(message1, message2)
    myWindow.showAndWait()
    ctrl.okClicked



