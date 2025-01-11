import scala.util.Random

class Level(val number: Int, val dungeonWidth: Int, val dungeonHeight: Int) {
  def generateMonsters(): List[Monster] = {
    val numMonsters = number * 2 // Increase the number of monsters with each level
    List.fill(numMonsters)(new Monster(Random.nextInt(dungeonWidth), Random.nextInt(dungeonHeight), 50 + number * 10))
  }

  def generateTreasures(): List[Treasure] = {
    val numTreasures = number * 2 // Increase the number of treasures with each level
    List.fill(numTreasures)(new Treasure("Gold Coin", 100 + number * 50))
  }
}