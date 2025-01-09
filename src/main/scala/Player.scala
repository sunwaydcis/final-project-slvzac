class Player(var x: Int, var y: Int, var health: Int) {
  var inventory: List[Item] = List(new Potion("Healing Potion", 20))
  var usedPotion: Boolean = false

  def move(direction: String, dungeon: Dungeon, monster: Monster): Unit = {
    dungeon.updatePosition(x, y, "_") // Clear current position

    direction match {
      case "w" if x > 0 => x -= 1
      case "s" if x < dungeon.height - 1 => x += 1
      case "a" if y > 0 => y -= 1
      case "d" if y < dungeon.width - 1 => y += 1
      case _ => // Invalid move or out of bounds
    }

    if (x == monster.x && y == monster.y) {
      combat(monster)
    } else {
      dungeon.updatePosition(x, y, "P") // Set new position
    }
  }

  def combat(monster: Monster): Unit = {
    println("Player encounters the monster!")
    while (health > 0 && monster.health > 0) {
      println("Choose action: attack, heavy, special, heal")
      val action = scala.io.StdIn.readLine()
      usedPotion = false
      action match {
        case "attack" => attack(monster)
        case "heavy" => heavyAttack(monster)
        case "special" => useSpecialAbility(monster)
        case "heal" => usePotion()
        case _ => println("Invalid action")
      }
      if (monster.health > 0 && !usedPotion) {
        monster.attack(this)
      }
    }
  }

  def attack(monster: Monster): Unit = {
    println("Player attacks the monster!")
    monster.health -= 10
    if (monster.health <= 0) {
      println("Monster is defeated!")
    } else {
      println(s"Monster's health: ${monster.health}")
    }
  }

  def heavyAttack(monster: Monster): Unit = {
    println("Player performs a heavy attack on the monster!")
    monster.health -= 20
    if (monster.health <= 0) {
      println("Monster is defeated!")
    } else {
      println(s"Monster's health: ${monster.health}")
    }
  }

  def useSpecialAbility(monster: Monster): Unit = {
    println("Player uses a special ability!")
    monster.health -= 30
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
        }
      case None =>
        println("No potions left!")
    }
  }
}