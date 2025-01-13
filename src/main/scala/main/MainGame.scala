package main

import controllers.DungeonViewController
import javafx.fxml.FXMLLoader
import javafx.scene.layout.AnchorPane
import javafx.scene.media.{AudioClip, Media, MediaPlayer}
import main.MainGame.stage
import models.{Dungeon, Monster, Player, Treasure}
import scalafx.application.{JFXApp3, Platform}
import scalafx.scene.Scene
import scalafx.scene.control.Alert
import scalafx.scene.control.Alert.AlertType
import scalafx.scene.layout.AnchorPane as SFXAnchorPane

import java.io.File
import scala.util.Random

object MainGame extends JFXApp3 {

  private var backgroundMusic: Option[AudioClip] = None

  override def start(): Unit = {
    showMenu()
    stage.setOnCloseRequest(_ => {
      Platform.exit()
      System.exit(0)
    })
  }

  def showMenu(): Unit = {
    val fxmlFile = new File("src/main/resources/MenuView.fxml")
    val loader = new FXMLLoader(fxmlFile.toURI.toURL)
    val root: AnchorPane = loader.load().asInstanceOf[AnchorPane]

    stage = new JFXApp3.PrimaryStage {
      title = "models.Dungeon Crawler"
      scene = new Scene(new SFXAnchorPane(root), 800, 600)
      resizable = false
    }
  }

  def startGame(): Unit = {
    val fxmlFile = new File("src/main/resources/DungeonView.fxml")
    val loader = new FXMLLoader(fxmlFile.toURI.toURL)
    val root: AnchorPane = loader.load().asInstanceOf[AnchorPane]
    val controller: DungeonViewController = loader.getController

    // Load and play background music using AudioClip
    val musicFile = new File("src/main/resources/assets/MusicBackdrop.mp3")
    if (musicFile.exists()) {
      println("Music file found: " + musicFile.getAbsolutePath)
      val audioClip = new AudioClip(musicFile.toURI.toString)
      audioClip.setCycleCount(AudioClip.INDEFINITE)
      audioClip.setVolume(1.0)
      audioClip.play()
      backgroundMusic = Some(audioClip)
    } else {
      println("Music file not found: " + musicFile.getAbsolutePath)
    }

    Platform.runLater {
      stage.scene = new Scene(new SFXAnchorPane(root), 800, 600)
    }

    new Thread(() => runGameLoop(controller)).start()
  }

  def runGameLoop(controller: DungeonViewController): Unit = {
    var levelNumber = 1
    var continueGame = true

    while (continueGame) {
      val gridSize = math.min(5 + (levelNumber - 1), 10)
      val dungeon = new Dungeon(gridSize, gridSize)
      println(s"Starting Level $levelNumber with grid size $gridSize x $gridSize")
      dungeon.printDungeon()
      Platform.runLater {
        controller.updateDungeon(dungeon)
        controller.updateLevel(levelNumber)
      }

      // Initialize player
      val player = new Player(0, 0)

      // Calculate the number of monsters based on the level number
      val numMonsters = if (levelNumber >= 3) 2 + (levelNumber - 2) else 2
      val monsters = List.fill(numMonsters)(new Monster(Random.nextInt(gridSize), Random.nextInt(gridSize), 50))

      // Generate and place treasures
      var treasures = generateTreasures(5, dungeon.width, dungeon.height)
      treasures.foreach(treasure => dungeon.updatePosition(treasure.x, treasure.y, "T"))

      // Update player, monster, and treasure positions
      Platform.runLater {
        controller.updatePlayerPosition(player.x, player.y)
        monsters.foreach(monster => controller.updateMonsterPosition(monster.x, monster.y))
        controller.updateDungeon(dungeon)
        controller.updateHealth(player.health)
      }

      // Handle player input for movement
      stage.scene().setOnKeyPressed { event =>
        val direction = event.getCode.getName.toLowerCase match {
          case "w" | "a" | "s" | "d" => event.getCode.getName.toLowerCase
          case _ => ""
        }
        if (direction.nonEmpty) {
          player.move(direction, dungeon, controller)
          monsters.foreach(_.move(dungeon, controller))

          // Check for collisions with monsters
          monsters.filter(monster => player.x == monster.x && player.y == monster.y).foreach { monster =>
            player.touchMonster()
            println(s"Player touches a monster at (${monster.x}, ${monster.y})! Health remaining: ${player.health}")
            if (player.health <= 0) {
              println("Player has touched monsters three times. Game Over!")
            }
          }

          // Check for collisions with treasures
          treasures.filter(treasure => player.x == treasure.x && player.y == treasure.y).foreach { treasure =>
            println(s"Player collects a ${treasure.name} worth ${treasure.value} points!")
            dungeon.clearPosition(treasure.x, treasure.y, "T")
            Platform.runLater {
              controller.updateDungeon(dungeon)
              controller.addPoints(treasure.value)
            }
            treasures = treasures.filterNot(_ == treasure)
          }

          // Check if all treasures are collected
          if (treasures.isEmpty) {
            println(s"Congratulations! Level $levelNumber completed.")
            levelNumber += 1
            println(s"Proceeding to Level $levelNumber...")
            continueGame = false
          }

          // Update health display
          Platform.runLater {
            controller.updateHealth(player.health)
          }
        }
      }

      // Wait for the player to finish the level or game over
      while (player.health > 0 && treasures.nonEmpty) {
        Thread.sleep(100) // Small delay to prevent busy-waiting
      }

      if (player.health <= 0) {
        println("Game Over! The player has been defeated.")
        backgroundMusic.foreach(_.stop()) // Stop the background music
        Platform.runLater(() => showGameOverAlert())
        continueGame = false
      } else if (treasures.isEmpty) {
        continueGame = true
      }
    }
  }

  def showGameOverAlert(): Unit = {
    val alert = new Alert(AlertType.Information) {
      initOwner(stage)
      title = "Game Over"
      headerText = "You have been defeated!"
      contentText = "Returning to the main menu..."
    }
    alert.showAndWait()
    showMenu()
  }

  def generateTreasures(numTreasures: Int, width: Int, height: Int): List[Treasure] = {
    List.fill(numTreasures) {
      val treasure = new Treasure("Gold Coin", 100)
      treasure.x = Random.nextInt(width)
      treasure.y = Random.nextInt(height)
      treasure
    }
  }
}