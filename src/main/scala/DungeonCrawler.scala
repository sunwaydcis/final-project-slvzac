import scala.util.Random

object DungeonCrawler {
  def main(args: Array[String]): Unit = {
    var levelNumber = 1
    var continueGame = true

    while (continueGame) {
      println(s"Starting Level $levelNumber")
      val level = new Level(levelNumber, 5 + levelNumber, 5 + levelNumber)
      val dungeon = new Dungeon(level.dungeonWidth, level.dungeonHeight)
      val player = new Player(0, 0, 100)
      var monsters = level.generateMonsters()
      var treasures = level.generateTreasures()

      dungeon.updatePosition(player.x, player.y, "P")
      monsters.foreach(monster => dungeon.updatePosition(monster.x, monster.y, "M"))
      dungeon.spawnTreasure(treasures)

      while (player.health > 0 && treasures.nonEmpty) {
        dungeon.updateDisplay()
        dungeon.printDungeon()
        val input = scala.io.StdIn.readLine("Enter move (w/a/s/d): ")
        player.move(input, dungeon, monsters)
        if (player.health > 0) {
          monsters.foreach(_.move(dungeon))
          monsters.filter(monster => player.x == monster.x && player.y == monster.y).foreach { monster =>
            println("Player encounters a monster!")
            val action = player.combat()
            handleCombat(action, player, monster, dungeon)
            if (monster.health <= 0) {
              println("Monster is defeated and disappears from the dungeon!")
              dungeon.updatePosition(monster.x, monster.y, "_")
              monsters = monsters.filterNot(_ == monster)
            }
          }
          treasures.filter(treasure => player.x == treasure.x && player.y == treasure.y).foreach { treasure =>
            println(s"Player collects a ${treasure.name} worth ${treasure.value} points!")
            dungeon.updatePosition(treasure.x, treasure.y, "_")
            treasures = treasures.filterNot(_ == treasure)
          }
        }
      }

      if (player.health <= 0) {
        println("Game Over! The player has been defeated.")
        continueGame = false
      } else if (treasures.isEmpty) {
        println(s"Congratulations! Level $levelNumber completed.")
        levelNumber += 1
      }
    }
  }

  def handleCombat(action: String, player: Player, monster: Monster, dungeon: Dungeon): Unit = {
    var continueCombat = true

    while (continueCombat && player.health > 0 && monster.health > 0) {
      action match {
        case "attack" =>
          val damage = player.attack(monster)
          println(s"Player attacks the monster, dealing $damage damage!")
          if (monster.health > 0) {
            println(s"Monster's health: ${monster.health}")
          }
        case "block" =>
          val (actualDamage, playerHealth) = player.blockAttack(monster)
          println(s"Player blocks the monster's attack, receiving only $actualDamage damage!")
          if (playerHealth <= 0) {
            println("Player is defeated!")
          } else {
            println(s"Player's health: $playerHealth")
          }
        case "special" =>
          if (player.specialCooldown == 0) {
            val damage = player.useSpecialAbility(monster)
            println(s"Player uses a special ability, dealing $damage damage!")
            if (monster.health > 0) {
              println(s"Monster's health: ${monster.health}")
            }
            player.specialCooldown = 3 // Reset cooldown
          } else {
            println(s"Special ability is on cooldown for ${player.specialCooldown} more turns.")
            player.skipMonsterTurn = true
          }
        case "heal" =>
          val (used, playerHealth) = player.usePotion()
          if (used) {
            println(s"Player uses a potion! Player's health: $playerHealth")
          } else {
            println("Potion use canceled.")
          }
        case _ => println("Invalid action")
      }

      if (monster.health > 0 && !player.usedPotion && !player.skipMonsterTurn) {
        val damage = if (Random.nextInt(6) == 0) {
          println("Monster performs special attack on player!")
          monster.specialAttack(player)
        } else {
          println("Monster attacks player!")
          monster.attack(player)
        }
        player.health -= damage
        println(s"Player's health: ${player.health}")
      }

      if (player.specialCooldown > 0) {
        player.specialCooldown -= 1 // Decrement cooldown each turn
      }

      if (player.health <= 0) {
        println("Player is defeated!")
        continueCombat = false
      } else if (monster.health <= 0) {
        println("Monster is defeated!")
        dungeon.updatePosition(monster.x, monster.y, "_")
        continueCombat = false
      } else {
        val nextAction = player.combat()
        handleCombat(nextAction, player, monster, dungeon)
      }
    }
  }
}