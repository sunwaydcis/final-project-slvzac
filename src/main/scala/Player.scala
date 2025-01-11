import scala.util.Random

class Player(var x: Int, var y: Int, var health: Int) {
  var inventory: List[Item] = List(new Potion("Healing Potion", 20))
  var usedPotion: Boolean = false
  var specialCooldown: Int = 0
  var skipMonsterTurn: Boolean = false

  def move(direction: String, dungeon: Dungeon, monsters: List[Monster]): Unit = {
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

  def combat(): String = {
    println("Choose action: attack, block, special, heal")
    scala.io.StdIn.readLine()
  }

  def attack(monster: Monster): Int = {
    val damage = 10 + Random.nextInt(11) // Random damage between 10 and 20
    monster.health -= damage
    damage
  }

  def blockAttack(monster: Monster): (Int, Int) = {
    val damage = if (Random.nextInt(6) == 0) {
      monster.specialAttack(this)
    } else {
      monster.attack(this)
    }
    val blockPercentage = Random.nextInt(101) // Random percentage between 0 and 100
    val blockedDamage = damage * blockPercentage / 100
    val actualDamage = damage - blockedDamage
    health -= actualDamage
    skipMonsterTurn = true
    (actualDamage, health)
  }

  def useSpecialAbility(monster: Monster): Int = {
    val damage = 20 + Random.nextInt(21) // RNG damage between 20 and 40
    monster.health -= damage
    damage
  }

  def usePotion(): (Boolean, Int) = {
    val potions = inventory.collect { case p: Potion => p }
    potions.headOption match {
      case Some(potion) =>
        println(s"Do you want to use a ${potion.name}? You have ${potions.size} left. (yes/no)")
        val confirmation = scala.io.StdIn.readLine()
        if (confirmation.toLowerCase == "yes") {
          health += potion.healingAmount
          inventory = inventory.filterNot(_ == potion)
          usedPotion = true
          (true, health)
        } else {
          skipMonsterTurn = true
          (false, health)
        }
      case None =>
        println("No potions left!")
        (false, health)
    }
  }
}