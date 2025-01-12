import scalafx.application.JFXApp3
import scalafx.scene.Scene
import scalafx.scene.layout.GridPane
import scalafxml.core.{FXMLLoader, NoDependencyResolver}

object GameGUI extends JFXApp3 {

  override def start(): Unit = {
    val resource = getClass.getResource("/DungeonView.fxml")
    val loader = new FXMLLoader(resource, NoDependencyResolver)
    val root: GridPane = loader.load().asInstanceOf[GridPane]
    val controller = loader.getController[DungeonViewController#Controller]

    // Example dungeon initialization
    val dungeon = new Dungeon(6, 6)
    controller.updateDungeon(dungeon)

    stage = new JFXApp3.PrimaryStage {
      title = "Dungeon Crawler"
      scene = new Scene(root)
    }
  }
}