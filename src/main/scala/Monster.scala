import scala.util.Random
// Monster AI MOVEMENT

class Monster(var x: Int, var y: Int, var health: Int) {
  def move(dungeon: Dungeon): Unit = {
    // Simple random movement logic (can be improved)
    val directions = List("w", "s", "a", "d")
    val direction = directions(scala.util.Random.nextInt(directions.length))

    dungeon.updatePosition(x, y, "_") // Clear current position

    direction match {
      case "w" if x > 0 => x -= 1
      case "s" if x < dungeon.height - 1 => x += 1
      case "a" if y > 0 => y -= 1
      case "d" if y < dungeon.width - 1 => y += 1
      case _ => // Invalid move or out of bounds
    }

    dungeon.updatePosition(x, y, "M") // Set new position
  }

  def attack(player: Player): Unit = {
    val damage = 2 + Random.nextInt(9) // RNG dmg between 2 and 10
    println(s"Monster attacks the player, inflicting $damage damage!")
    player.health -= damage
    if (player.health <= 0) {
      println("Player is defeated!")
    } else {
      println(s"Player's health: ${player.health}")
    }
  }

  def specialAttack(player: Player): Unit = {
    val damage = 2 + Random.nextInt(9) // RNG
    println(s"Monster performs a special attack on the player, inflicting $damage damage!")
    player.health -= 20
    if (player.health <= 0) {
      println("Player is defeated!")
    } else {
      println(s"Player's health: ${player.health}")
    }
  }
}