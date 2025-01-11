abstract class Item(val name: String)

class Treasure(name: String, val value: Int) extends Item(name) {
  var x: Int = 0
  var y: Int = 0
}

class Potion(name: String, val healingAmount: Int) extends Item(name)