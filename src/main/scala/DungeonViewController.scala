import javafx.fxml.{FXML, Initializable}
import javafx.scene.control.Label
import javafx.scene.image.{Image, ImageView}
import javafx.scene.layout.{StackPane, GridPane}
import java.net.URL
import java.util.ResourceBundle
import javafx.animation.{KeyFrame, Timeline}
import javafx.util.Duration
import javafx.scene.image.ImageView
import javafx.animation.Animation

class DungeonViewController extends Initializable {

  @FXML private var dungeonGrid: GridPane = _
  @FXML private var pointsLabel: Label = _
  @FXML private var levelLabel: Label = _
  @FXML private var healthLabel: Label = _
  @FXML private var continueLabel: Label = _

  private val walkableTile = new Image(getClass.getResource("/assets/Walkable_Tile.png").toString)
  private val playerImages = Array(
    new Image(getClass.getResource("/assets/player_1.png").toString),
    new Image(getClass.getResource("/assets/player_2.png").toString),
    new Image(getClass.getResource("/assets/player_3.png").toString),
    new Image(getClass.getResource("/assets/player_4.png").toString)
  )
  private val monsterImages = Array(
    new Image(getClass.getResource("/assets/monster_1.png").toString),
    new Image(getClass.getResource("/assets/monster_2.png").toString),
    new Image(getClass.getResource("/assets/monster_3.png").toString),
    new Image(getClass.getResource("/assets/monster_4.png").toString)
  )
  private val treasureImages = Array(
    new Image(getClass.getResource("/assets/chest_1.png").toString),
    new Image(getClass.getResource("/assets/chest_2.png").toString),
    new Image(getClass.getResource("/assets/chest_3.png").toString),
    new Image(getClass.getResource("/assets/chest_4.png").toString)
  )

  private var points: Int = 0
  private var level: Int = 1

  override def initialize(location: URL, resources: ResourceBundle): Unit = {
    // Initialize the grid with walkable tiles
    for (i <- 0 until 10; j <- 0 until 10) {
      val imageView = new ImageView(walkableTile)
      imageView.setSmooth(false)
      val stackPane = new StackPane()
      stackPane.getChildren.add(imageView)
      stackPane.prefWidthProperty().bind(dungeonGrid.widthProperty().divide(10))
      stackPane.prefHeightProperty().bind(dungeonGrid.heightProperty().divide(10))
      imageView.fitWidthProperty().bind(stackPane.prefWidthProperty())
      imageView.fitHeightProperty().bind(stackPane.prefHeightProperty())
      imageView.setPreserveRatio(true)
      GridPane.setConstraints(stackPane, i, j)
      dungeonGrid.add(stackPane, i, j)
    }
  }

  def startAnimation(imageView: ImageView, images: Array[Image]): Unit = {
    val timeline = new Timeline(new KeyFrame(Duration.millis(250), _ => {
      val currentIndex = images.indexOf(imageView.getImage)
      val nextIndex = (currentIndex + 1) % images.length
      imageView.setImage(images(nextIndex))
    }))
    timeline.setCycleCount(Animation.INDEFINITE)
    timeline.play()
  }

  def updateDungeon(dungeon: Dungeon): Unit = {
    dungeonGrid.getChildren.clear()
    for (x <- 0 until dungeon.width; y <- 0 until dungeon.height) {
      val symbols = dungeon.grid(x)(y)
      val stackPane = new StackPane()
      symbols.foreach {
        case "P" =>
          val playerImageView = new ImageView(playerImages(0))
          stackPane.getChildren.add(playerImageView)
          startAnimation(playerImageView, playerImages)
        case "M" =>
          val monsterImageView = new ImageView(monsterImages(0))
          stackPane.getChildren.add(monsterImageView)
          startAnimation(monsterImageView, monsterImages)
        case "T" =>
          val treasureImageView = new ImageView(treasureImages(0))
          stackPane.getChildren.add(treasureImageView)
          startAnimation(treasureImageView, treasureImages)
        case _ => stackPane.getChildren.add(new ImageView(walkableTile))
      }
      stackPane.prefWidthProperty().bind(dungeonGrid.widthProperty().divide(dungeon.width))
      stackPane.prefHeightProperty().bind(dungeonGrid.heightProperty().divide(dungeon.height))
      stackPane.getChildren.forEach { imageView =>
        imageView.asInstanceOf[ImageView].fitWidthProperty().bind(stackPane.prefWidthProperty())
        imageView.asInstanceOf[ImageView].fitHeightProperty().bind(stackPane.prefHeightProperty())
        imageView.asInstanceOf[ImageView].setPreserveRatio(true)
      }
      GridPane.setConstraints(stackPane, x, y)
      dungeonGrid.add(stackPane, x, y)
    }
  }

  def updatePlayerPosition(x: Int, y: Int): Unit = {
    val playerImageView = new ImageView(playerImages(0))
    playerImageView.setSmooth(false)
    playerImageView.fitWidthProperty().bind(dungeonGrid.widthProperty().divide(dungeonGrid.getColumnCount))
    playerImageView.fitHeightProperty().bind(dungeonGrid.heightProperty().divide(dungeonGrid.getRowCount))
    playerImageView.setPreserveRatio(true)
    val stackPane = dungeonGrid.getChildren.get(y * dungeonGrid.getColumnCount + x).asInstanceOf[StackPane]
    stackPane.getChildren.add(playerImageView)
    startAnimation(playerImageView, playerImages)
  }

  def updateMonsterPosition(x: Int, y: Int): Unit = {
    val monsterImageView = new ImageView(monsterImages(0))
    monsterImageView.setSmooth(false)
    monsterImageView.fitWidthProperty().bind(dungeonGrid.widthProperty().divide(dungeonGrid.getColumnCount))
    monsterImageView.fitHeightProperty().bind(dungeonGrid.heightProperty().divide(dungeonGrid.getRowCount))
    monsterImageView.setPreserveRatio(true)
    val stackPane = dungeonGrid.getChildren.get(y * dungeonGrid.getColumnCount + x).asInstanceOf[StackPane]
    stackPane.getChildren.add(monsterImageView)
    startAnimation(monsterImageView, monsterImages)
  }

  def addPoints(points: Int): Unit = {
    val currentPoints = pointsLabel.getText.split(": ")(1).toInt
    pointsLabel.setText(s"Points: ${currentPoints + points}")
  }

  def updateLevel(level: Int): Unit = {
    levelLabel.setText(s"Level: $level")
  }

  def updateHealth(health: Int): Unit = {
    healthLabel.setText(s"Health: $health")
  }

  def showContinueMessage(): Unit = {
    continueLabel.setVisible(true)
  }
}