package my.quizapp

import scalafx.application.JFXApp3
import scalafx.application.JFXApp3.PrimaryStage
import javafx.application.Application
import javafx.fxml.FXMLLoader
import javafx.scene.*
import javafx.stage.Stage
import javafx.scene as jfxs
import scalafx.Includes.*
import scalafx.scene.Scene

object MainQuizApp extends JFXApp3:

  var roots: Option[scalafx.scene.layout.BorderPane] = None

  override def start(): Unit =
    try
      //transform path of RootLayout.fxml to URI for resource location
      val rootResource = getClass.getResource("view/RootLayout.fxml")
      //initialize the loader object
      val loader = new FXMLLoader(rootResource)
      //load root layout from fxml file
      loader.load()// parse fxml, create all objects from fxml
//      val javaRoot = loader.getRoot[jfxs.layout.BorderPane]()
//      roots = Some(new scalafx.scene.layout.BorderPane(javaRoot))
      roots = Option(loader.getRoot[jfxs.layout.BorderPane])
      stage = new PrimaryStage():
        title = "QuizApp"
        scene = new Scene(roots.get)
      showMainMenu()
    catch
      case e: Exception =>
        println("Failed to load main menu : " + e.getMessage)
        e.printStackTrace()

  def showMainMenu(): Unit =
    try
      val resource = getClass.getResource("view/MainMenu.fxml")
      val loader = new FXMLLoader(resource)
      loader.load()
      val roots = loader.getRoot[jfxs.layout.AnchorPane]
      this.roots.get.center = roots
//      val centerPane = loader.getRoot[jfxs.layout.AnchorPane]()
//      this.roots.get.setCenter(centerPane)
//      val javaPane = loader.getRoot[jfxs.layout.AnchorPane]()
//      val fxPane = new scalafx.scene.layout.AnchorPane(javaPane)
//      this.roots.get.center = fxPane
    catch
      case e: Exception =>
        println("Failed to load main menu : " + e.getMessage)
        e.printStackTrace()


