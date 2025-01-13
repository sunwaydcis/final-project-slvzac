import scala.util.Random

class Monster(var x: Int, var y: Int, var health: Int) {
  def move(dungeon: Dungeon, controller: DungeonViewController): Unit = {
    // Clear current position
    dungeon.clearPosition(x, y, "M")
    controller.updateDungeon(dungeon)

    // Move randomly
    val direction = Random.nextInt(4)
    direction match {
      case 0 if x < dungeon.width - 1 => x += 1 // Move right
      case 1 if x > 0 => x -= 1 // Move left
      case 2 if y < dungeon.height - 1 => y += 1 // Move down
      case 3 if y > 0 => y -= 1 // Move up
      case _ => // Stay in place
    }

    // Set new position
    dungeon.updatePosition(x, y, "M")
    controller.updateMonsterPosition(x, y)
  }
}