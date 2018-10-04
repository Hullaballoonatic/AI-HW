package model

interface Board {
    val positions: List<Position>
    val winner: Player?

    operator fun get(position: Int) = positions[position]
    operator fun get(coords: Pair<Int,Int>) = positions.first { it.x == coords.first && it.y == coords.second }
}