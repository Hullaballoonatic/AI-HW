package model

interface Board {
    val positions: List<Position>
    val winner: Player?

    val isEnded get() = winner != null

    operator fun get(position: Int) = positions[position]
    operator fun get(position: Position) = positions.firstOrNull { it.number == position.number }
}