import scala.util.Random

object DungeonCrawler {
  def main(args: Array[String]): Unit = {
    val dungeon = new Dungeon(5, 5)
    val player = new Player(0, 0, 100)
    val monster = new Monster(4, 4, 50)

    dungeon.updatePosition(player.x, player.y, "P")
    dungeon.updatePosition(monster.x, monster.y, "M")

    while (player.health > 0 && monster.health > 0) {
      dungeon.printDungeon()
      val input = scala.io.StdIn.readLine("Enter move (w/a/s/d): ")
      player.move(input, dungeon, monster)
      if (player.health > 0 && monster.health > 0) {
        monster.move(dungeon)
        if (player.x == monster.x && player.y == monster.y) {
          println("Player encounters the monster!")
          val action = player.combat()
          handleCombat(action, player, monster)
        }
      }
    }

    if (player.health <= 0) {
      println("Game Over! The player has been defeated.")
    } else if (monster.health <= 0) {
      println("Congratulations! The monster has been defeated.")
    }
  }

  // Combat logic and announcements
  def handleCombat(action: String, player: Player, monster: Monster): Unit = {
    var continueCombat = true

    // Player's turn:
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

      // Monster's turn:
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

      if (player.specialCooldown > 0 && !player.skipMonsterTurn) {
        player.specialCooldown -= 1 // Decrement cooldown each turn
      }

      if (player.health <= 0) {
        println("Player is defeated!")
        continueCombat = false
      } else if (monster.health <= 0) {
        println("Monster is defeated!")
        continueCombat = false
      } else {
        // Prompt for the next action
        val nextAction = player.combat()
        handleCombat(nextAction, player, monster)
      }
    }
  }
}