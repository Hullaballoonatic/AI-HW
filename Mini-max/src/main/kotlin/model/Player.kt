package model

enum class Player(val label: String) {
    X("X"),
    O("O"),
    NONE(" ");

    override fun toString() = label
}