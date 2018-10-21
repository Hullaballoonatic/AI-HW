package model

class Position(val number: Int, owner: Player = Player.NONE) {
    var owner = owner
        set(v) {
            label.set(v.label)
            field = v
        }

    private val coords = when(number) {
        1 -> 0 to 0
        2 -> 0 to 1
        3 -> 0 to 2
        4 -> 1 to 0
        5 -> 1 to 1
        6 -> 1 to 2
        7 -> 2 to 0
        8 -> 2 to 1
        9 -> 2 to 2
        else -> throw(Exception("invalid id"))
    }

    val col = coords.second
    val row = coords.first

    val label = SimpleStringProperty(number.toString())

    val isActionable: Boolean get() = owner == Player.NONE

    override fun toString() = "$owner"
}