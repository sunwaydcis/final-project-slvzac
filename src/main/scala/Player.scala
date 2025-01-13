class Player(var x: Int, var y: Int, var health: Int = 3) {
  var inventory: List[Item] = List(new Potion("Healing Potion", 20))
  var usedPotion: Boolean = false
  var specialCooldown: Int = 0
  var skipMonsterTurn: Boolean = false
  var monsterTouches: Int = 0

  def move(direction: String, dungeon: Dungeon, controller: DungeonViewController): Unit = {
    // Clear current position
    dungeon.clearPosition(x, y, "P")
    controller.updateDungeon(dungeon)

    direction match {
      case "w" if y > 0 => y -= 1 // Move up
      case "s" if y < dungeon.height - 1 => y += 1 // Move down
      case "a" if x > 0 => x -= 1 // Move left
      case "d" if x < dungeon.width - 1 => x += 1 // Move right
      case _ => // Invalid move or out of bounds
    }

    // Set new position
    dungeon.updatePosition(x, y, "P")
    controller.updatePlayerPosition(x, y)

    // Print the player's new position for debugging
    println(s"Player moved to position: ($x, $y)")
  }

  def touchMonster(): Unit = {
    monsterTouches += 1
    health -= 1
  }
}