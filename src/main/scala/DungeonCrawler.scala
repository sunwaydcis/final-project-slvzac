object DungeonCrawler {
  def main(args: Array[String]): Unit = {
    var levelNumber = 1
    var continueGame = true

    while (continueGame) {
      val gridSize = math.min(5 + (levelNumber - 1), 10)
      println(s"Starting Level $levelNumber with grid size $gridSize x $gridSize")
      val level = new Level(levelNumber, gridSize, gridSize)
      val dungeon = new Dungeon(level.dungeonWidth, level.dungeonHeight)
      val player = new Player(0, 0, 100)
      var monsters = level.generateMonsters()
      var treasures = level.generateTreasures()

      dungeon.updatePosition(player.x, player.y, "P")
      monsters.foreach(monster => dungeon.updatePosition(monster.x, monster.y, "M"))
      dungeon.spawnTreasure(treasures)

      // Initialize the GUI
      val controller = new DungeonViewController()
      controller.updateDungeon(dungeon)
      controller.updatePlayerPosition(player.x, player.y)
      monsters.foreach(monster => controller.updateMonsterPosition(monster.x, monster.y))

      while (player.health > 0 && treasures.nonEmpty) {
        dungeon.updateDisplay()
        dungeon.printDungeon()
        val input = scala.io.StdIn.readLine("Enter move (w/a/s/d): ")
        player.move(input, dungeon, controller)
        if (player.health > 0) {
          monsters.foreach(_.move(dungeon, controller)) // Pass dungeon and controller
          monsters.filter(monster => player.x == monster.x && player.y == monster.y).foreach { monster =>
            player.monsterTouches += 1
            println(s"Player touches a monster at (${monster.x}, ${monster.y})! Touch count: ${player.monsterTouches}")
            if (player.monsterTouches >= 3) {
              println("Player has touched monsters three times. Game Over!")
              player.health = 0
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
        println(s"Proceeding to Level $levelNumber...")
      }
    }
  }
}