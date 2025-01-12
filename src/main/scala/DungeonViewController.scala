import javafx.fxml.{FXML, Initializable}
import javafx.scene.image.{Image, ImageView}
import javafx.scene.layout.{StackPane, GridPane}
import java.net.URL
import java.util.ResourceBundle

class DungeonViewController extends Initializable {

  @FXML private var dungeonGrid: GridPane = _

  private val walkableTile = new Image("file:assets/Walkable_Tile.png")
  private val tileSize = 50 // Increase the tile size for better visibility

  override def initialize(location: URL, resources: ResourceBundle): Unit = {
    // Initialize the grid with walkable tiles
    for (i <- 0 until 5; j <- 0 until 5) {
      val imageView = new ImageView(walkableTile) {
        setFitWidth(tileSize)
        setFitHeight(tileSize)
        setPreserveRatio(true)
      }
      val stackPane = new StackPane() {
        getChildren.add(imageView)
        setStyle("-fx-border-color: red;") // Add a border to make the StackPane more visible
        setMinWidth(tileSize)
        setMinHeight(tileSize)
      }
      GridPane.setConstraints(stackPane, i, j)
      dungeonGrid.add(stackPane, i, j)
      println(s"Added StackPane at position ($i, $j) with ImageView")
    }
    println("GridPane initialized with walkable tiles.")
  }

  def updateDungeon(dungeon: Dungeon): Unit = {
    dungeonGrid.getChildren.clear()
    println("Updating dungeon grid:")
    for (x <- 0 until dungeon.width; y <- 0 until dungeon.height) {
      val symbol = dungeon.grid(x)(y)
      println(s"Position ($x, $y): $symbol")
      val imageView = new ImageView(walkableTile) {
        setFitWidth(tileSize)
        setFitHeight(tileSize)
        setPreserveRatio(true)
      }
      val stackPane = new StackPane() {
        getChildren.add(imageView)
        setStyle("-fx-border-color: red;") // Add a border to make the StackPane more visible
        setMinWidth(tileSize)
        setMinHeight(tileSize)
      }
      GridPane.setConstraints(stackPane, x, y)
      dungeonGrid.add(stackPane, x, y)
      println(s"Added StackPane at position ($x, $y) with ImageView")
    }
  }
}