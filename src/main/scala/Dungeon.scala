import scala.util.Random

class Dungeon(val width: Int, val height: Int) {
  val grid: Array[Array[String]] = Array.fill(width, height)("_")
  val treasures = scala.collection.mutable.Set[(Int, Int)]()

  def printDungeon(): Unit = {
    for (row <- grid) {
      println(row.mkString(" "))
    }
    println()
  }

  def updatePosition(x: Int, y: Int, symbol: String): Unit = {
    if (symbol == "T") {
      treasures.add((x, y))
    } else if (symbol == "_") {
      treasures.remove((x, y))
    }
    grid(x)(y) = symbol
  }

  def spawnTreasure(treasures: List[Treasure]): Unit = {
    treasures.foreach { treasure =>
      var placed = false
      while (!placed) {
        val x = Random.nextInt(width)
        val y = Random.nextInt(height)
        if (grid(x)(y) == "_") {
          updatePosition(x, y, "T")
          treasure.x = x
          treasure.y = y
          placed = true
        }
      }
    }
  }

  def updateDisplay(): Unit = {
    for (x <- 0 until width; y <- 0 until height) {
      if (treasures.contains((x, y))) {
        grid(x)(y) = "T"
      }
    }
  }
}