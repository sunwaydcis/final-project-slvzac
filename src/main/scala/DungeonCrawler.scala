// src/main/scala/DungeonCrawler.scala
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
          player.combat(monster)
        }
      }
    }

    if (player.health <= 0) {
      println("Game Over! The player has been defeated.")
    } else if (monster.health <= 0) {
      println("Congratulations! The monster has been defeated.")
    }
  }
}