object DungeonCrawler {
  var gameRunning = true // Game running flag

  def main(args: Array[String]): Unit = {
    val dungeon = new Dungeon(5, 5)
    val player = new Player(0, 0, 100)
    val monster = new Monster(4, 4, 50)

    dungeon.updatePosition(player.x, player.y, "P")
    dungeon.updatePosition(monster.x, monster.y, "M")

    while (gameRunning) {
      dungeon.printDungeon()
      val input = scala.io.StdIn.readLine("Enter move (w/a/s/d): ")
      player.move(input, dungeon, monster)
      if (player.health <= 0 || monster.health <= 0) {
        gameRunning = false
      } else {
        monster.move(dungeon)
        if (player.x == monster.x && player.y == monster.y) {
          combatScreen(player, monster)
          if (player.health <= 0 || monster.health <= 0) {
            gameRunning = false
          }
        }
      }
    }

    if (player.health <= 0) {
      println("Game Over! The player has been defeated.")
    } else if (monster.health <= 0) {
      println("Congratulations! The monster has been defeated.")
    }
  }

  def combatScreen(player: Player, monster: Monster): Unit = {
    println("Combat Screen: Player vs Monster")
    while (player.health > 0 && monster.health > 0) {
      println(s"Player Health: ${player.health}, Monster Health: ${monster.health}")
      println("Choose action: attack, heavy, special, heal")
      val action = scala.io.StdIn.readLine()
      action match {
        case "attack" => player.attack(monster)
        case "heavy" => player.heavyAttack(monster)
        case "special" => player.useSpecialAbility(monster)
        case "heal" => player.usePotion()
        case _ => println("Invalid action")
      }
      if (monster.health > 0) {
        monster.attack(player)
      }
    }
  }
}