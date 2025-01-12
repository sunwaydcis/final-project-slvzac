class Dungeon(val width: Int, val height: Int) {
  val grid: Array[Array[String]] = Array.fill(width, height)("_")

  def updatePosition(x: Int, y: Int, symbol: String): Unit = {
    grid(x)(y) = symbol
  }

  def updateDisplay(): Unit = {
    for (x <- 0 until width; y <- 0 until height) {
      grid(x)(y) = "_"
    }
  }

  def spawnTreasure(treasures: Seq[Treasure]): Unit = {
    treasures.foreach { treasure =>
      grid(treasure.x)(treasure.y) = "T"
    }
  }

  def printDungeon(): Unit = {
    for (row <- grid) {
      println(row.mkString(" "))
    }
  }
}