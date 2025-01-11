import scala.util.Random

class Dungeon(val width: Int, val height: Int) {
  val grid: Array[Array[String]] = Array.fill(width, height)("_")
  val treasures = scala.collection.mutable.Set[(Int, Int)]()
  val monsters = scala.collection.mutable.Set[(Int, Int)]()
  var playerPosition: (Int, Int) = (0, 0)

  def printDungeon(): Unit = {
    for (row <- grid) {
      println(row.mkString(" "))
    }
    println()
  }

  def updatePosition(x: Int, y: Int, symbol: String): Unit = {
    symbol match {
      case "T" => treasures.add((x, y))
      case "M" => monsters.add((x, y))
      case "P" => playerPosition = (x, y)
      case "_" =>
        treasures.remove((x, y))
        monsters.remove((x, y))
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
      grid(x)(y) = "_"
    }
    treasures.foreach { case (x, y) => grid(x)(y) = "T" }
    monsters.foreach { case (x, y) => grid(x)(y) = "M" }
    val (px, py) = playerPosition
    grid(px)(py) = "P"
  }
}