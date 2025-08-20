package farmgame
import farmgame.model.{Farm, Villager}
import farmgame.view.*
import javafx.fxml.FXMLLoader
import scalafx.application.JFXApp3
import scalafx.application.JFXApp3.PrimaryStage
import scalafx.scene.Scene
import scalafx.Includes.*
import scalafx.stage.Modality.ApplicationModal
import scalafx.stage.{Stage, Window}

import java.net.URL

object FieldsOfPlenty extends JFXApp3:
  var rootPane: Option[javafx.scene.layout.BorderPane] = None
  override def start(): Unit = {
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
  }

  def showMainMenu(): Unit = {
    val mainMenu: URL = getClass.getResource("/farmgame/view/MainMenu.fxml")
    val loader = new FXMLLoader(mainMenu)
    val pane = loader.load[javafx.scene.layout.AnchorPane]()
    rootPane.foreach(_.setCenter(pane))
  }

  def showFarm(): Unit =
    showFarm(createDefaultFarm())

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

  def createDefaultFarm(): Farm =
    Farm(3, 3, List(Villager("Alice"), Villager("Bob")))

  def showGameOver(days: Int): Boolean =
    val gameOver = getClass.getResource("/farmgame/view/GameOver.fxml")
    val loader = new FXMLLoader(gameOver)
    loader.load()
    val pane = loader.getRoot[javafx.scene.layout.AnchorPane]()
    val myWindow = new Stage():
      initOwner(stage)
      initModality(ApplicationModal)
      title = "Game Over"
      scene = new Scene():
        root = pane
    val ctrl = loader.getController[GameOverController]()
    ctrl.setScore(days)

    ctrl.stage = Option(myWindow)
    myWindow.showAndWait() //popup
    ctrl.okClicked

  //popup dialog
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
  
  //def showVillagerInfo()//penalties?
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
    



