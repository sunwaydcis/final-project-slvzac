abstract class Item(val name: String)

class Treasure(name: String, val value: Int) extends Item(name)

class Potion(name: String, val healingAmount: Int) extends Item(name)