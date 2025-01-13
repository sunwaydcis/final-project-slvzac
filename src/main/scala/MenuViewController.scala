import javafx.fxml.FXML
import javafx.scene.control.Button
import javafx.scene.image.ImageView
import javafx.scene.image.Image
import scalafx.application.Platform

class MenuViewController {

  @FXML private var menuImage: ImageView = _
  @FXML private var titleImage: ImageView = _
  @FXML private var startButton: Button = _

  @FXML
  def initialize(): Unit = {
    menuImage.setImage(new Image(getClass.getResource("/assets/Menu.png").toString))
    titleImage.setImage(new Image(getClass.getResource("/assets/TitleScreen.png").toString))
    startButton.setOnAction(_ => Platform.runLater(() => GameGUI.startGame()))
  }
}