import scalafx.application.JFXApp3
import scalafx.scene.Scene
import javafx.fxml.FXMLLoader
import javafx.scene.layout.AnchorPane
import scalafx.scene.layout.AnchorPane as SFXAnchorPane

object GameGUI extends JFXApp3 {

  override def start(): Unit = {
    val loader = new FXMLLoader(getClass.getResource("/DungeonView.fxml"))
    val root: AnchorPane = loader.load().asInstanceOf[AnchorPane]
    val controller: DungeonViewController = loader.getController()

    // Example dungeon initialization
    val dungeon = new Dungeon(6, 6)
    println("Dungeon initialized:")
    dungeon.printDungeon()
    controller.updateDungeon(dungeon)

    stage = new JFXApp3.PrimaryStage {
      title = "Dungeon Crawler"
      scene = new Scene(new SFXAnchorPane(root))
    }
  }
}