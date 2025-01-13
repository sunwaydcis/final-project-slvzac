package models

import models.Treasure

class Dungeon(val width: Int, val height: Int) {
  val grid: Array[Array[List[String]]] = Array.fill(width, height)(List("_"))
  var treasures: List[Treasure] = List()

  def updatePosition(x: Int, y: Int, symbol: String): Unit = {
    grid(x)(y) = grid(x)(y).filter(_ != symbol) :+ symbol
  }

  def clearPosition(x: Int, y: Int, symbol: String): Unit = {
    grid(x)(y) = grid(x)(y).filter(_ != symbol)
    if (grid(x)(y).isEmpty) grid(x)(y) = List("_")
  }

  def updateDisplay(): Unit = {
    for (x <- 0 until width; y <- 0 until height) {
      grid(x)(y) = List("_")
    }
    treasures.foreach(treasure => grid(treasure.x)(treasure.y) = List("T"))
  }

  def spawnTreasure(treasures: Seq[Treasure]): Unit = {
    this.treasures = treasures.toList
    treasures.foreach { treasure =>
      grid(treasure.x)(treasure.y) = List("T")
    }
  }

  def printDungeon(): Unit = {
    for (row <- grid) {
      println(row.map(_.mkString("")).mkString(" "))
    }
  }
}