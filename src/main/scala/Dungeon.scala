class Dungeon(val width: Int, val height: Int) {
  val grid: Array[Array[String]] = Array.fill(width, height)("_")

  def printDungeon(): Unit = {
    for (row <- grid) {
      println(row.mkString(" "))
    }
    println()
  }

  def updatePosition(x: Int, y: Int, symbol: String): Unit = {
    grid(x)(y) = symbol
  }
}