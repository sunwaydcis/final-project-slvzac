import scala.util.Random

class Monster(var x: Int, var y: Int, var health: Int) {
  def move(dungeon: Dungeon): Unit = {
    // Simple random movement logic (can be improved)
    val directions = List("w", "s", "a", "d")
    val direction = directions(Random.nextInt(directions.length))

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

  def attack(player: Player): Int = {
    2 + Random.nextInt(9) // RNG damage between 2 and 10
  }

  def specialAttack(player: Player): Int = {
    4 + Random.nextInt(12) // RNG damage between 4 and 15
  }
}