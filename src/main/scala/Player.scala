import scala.util.Random

class Player(var x: Int, var y: Int, var health: Int) {
  var inventory: List[Item] = List(new Potion("Healing Potion", 20))
  var usedPotion: Boolean = false
  var specialCooldown: Int = 0
  var skipMonsterTurn: Boolean = false

  def move(direction: String, dungeon: Dungeon, monster: Monster): Unit = {
    dungeon.updatePosition(x, y, "_") // Clear current position

    direction match {
      case "w" if x > 0 => x -= 1
      case "s" if x < dungeon.height - 1 => x += 1
      case "a" if y > 0 => y -= 1
      case "d" if y < dungeon.width - 1 => y += 1
      case _ => // Invalid move or out of bounds
    }

    dungeon.updatePosition(x, y, "P") // Set new position
  }

  def combat(monster: Monster): Unit = {
    println("Player encounters the monster!")
    while (health > 0 && monster.health > 0) {
      println("Choose action: attack, block, special, heal")
      val action = scala.io.StdIn.readLine()
      usedPotion = false
      skipMonsterTurn = false
      action match {
        case "attack" => attack(monster)
        case "block" => blockAttack(monster)
        case "special" =>
          if (specialCooldown == 0) {
            useSpecialAbility(monster)
            specialCooldown = 3 // Reset cooldown
          } else {
            println(s"Special ability is on cooldown for $specialCooldown more turns.")
            skipMonsterTurn = true
          }
        case "heal" => usePotion()
        case _ => println("Invalid action")
      }
      if (monster.health > 0 && !usedPotion && !skipMonsterTurn) {
        monster.attack(this)
      }
      if (specialCooldown > 0 && !skipMonsterTurn) {
        specialCooldown -= 1 // Decrement cooldown each turn
      }
    }
  }

  def attack(monster: Monster): Unit = {
    val damage = 5 + Random.nextInt(6)// Random damage between 5 and 10
    println(s"Player attacks the monster, inflicting $damage damage!")
    monster.health -= damage
    if (monster.health <= 0) {
      println("Monster is defeated!")
    } else {
      println(s"Monster's health: ${monster.health}")
    }
  }

  def blockAttack(monster: Monster): Unit = {
    println("Player blocks the monster's attack!")
    // Reduce the damage taken by half
    val damage = 5
    health -= damage
    if (health <= 0) {
      println("Player is defeated!")
    } else {
      println(s"Player's health: ${health}")
    }
  }

  def useSpecialAbility(monster: Monster): Unit = {
    val damage = 20 +  Random.nextInt(21)// Random damage between 20 and 40
    println(s"Player uses a special ability inflicting $damage damage!")
    monster.health -= damage
    if (monster.health <= 0) {
      println("Monster is defeated!")
    } else {
      println(s"Monster's health: ${monster.health}")
    }
  }

  def usePotion(): Unit = {
    val potions = inventory.collect { case p: Potion => p }
    potions.headOption match {
      case Some(potion) =>
        println(s"Do you want to use a ${potion.name}? You have ${potions.size} left. (yes/no)")
        val confirmation = scala.io.StdIn.readLine()
        if (confirmation.toLowerCase == "yes") {
          println(s"Player uses a ${potion.name}!")
          health += potion.healingAmount
          inventory = inventory.filterNot(_ == potion)
          usedPotion = true
          println(s"Player's health: $health")
        } else {
          println("Potion use canceled.")
          skipMonsterTurn = true
        }
      case None =>
        println("No potions left!")
    }
  }
}