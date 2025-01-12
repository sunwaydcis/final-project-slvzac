import scalafx.scene.image.{Image, ImageView}
import scalafx.scene.layout.GridPane
import scalafxml.core.macros.sfxml
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._
import scala.concurrent.{Future, blocking}

@sfxml
class DungeonViewController(private val dungeonGrid: GridPane) {

  private val playerImages = Seq(
    new Image("file:assets/player_1.png"),
    new Image("file:assets/player_2.png"),
    new Image("file:assets/player_3.png"),
    new Image("file:assets/player_4.png")
  )

  private val monsterImages = Seq(
    new Image("file:assets/monster_1.png"),
    new Image("file:assets/monster_2.png"),
    new Image("file:assets/monster_3.png"),
    new Image("file:assets/monster_4.png")
  )

  private val treasureImages = Seq(
    new Image("file:assets/chest_1.png"),
    new Image("file:assets/chest_2.png"),
    new Image("file:assets/chest_3.png"),
    new Image("file:assets/chest_4.png")
  )

  private var animationIndex = 0

  def initialize(): Unit = {
    // Initialize the dungeon grid with images or other elements
    startAnimation()
  }

  def updateDungeon(dungeon: Dungeon): Unit = {
    dungeonGrid.children.clear()
    for (x <- 0 until dungeon.width; y <- 0 until dungeon.height) {
      val symbol = dungeon.grid(x)(y)
      val imageView = new ImageView(symbol match {
        case "P" => playerImages(animationIndex)
        case "M" => monsterImages(animationIndex)
        case "T" => treasureImages(animationIndex)
        case _ => new Image("file:assets/empty.png")
      }) {
        fitWidth = 16
        fitHeight = 16
      }
      dungeonGrid.add(imageView, x, y)
    }
  }

  private def startAnimation(): Unit = {
    Future {
      while (true) {
        blocking {
          Thread.sleep(250) // Change frame every 250ms
          animationIndex = (animationIndex + 1) % 4
          updateDungeon(currentDungeon) // Assuming currentDungeon is a reference to the current dungeon state
        }
      }
    }
  }
}