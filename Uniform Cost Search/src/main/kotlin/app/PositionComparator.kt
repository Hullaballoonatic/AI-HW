package app

class PositionComparator : Comparator<Position> {
    override fun compare (a: Position, b: Position) = when {
        a.distance > b.distance -> 1
        a.distance < b.distance -> -1
        else -> 0
    }
}