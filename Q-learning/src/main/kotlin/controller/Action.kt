package controller

enum class Action(val label: Char) {
    MOVE_LEFT('˂'),
    MOVE_RIGHT('˃'),
    MOVE_UP('˄'),
    MOVE_DOWN('˅');

    override fun toString(): String = label.toString()
}