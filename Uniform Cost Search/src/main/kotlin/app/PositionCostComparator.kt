package app

class PositionCostComparator : Comparator<Position> {
    override fun compare(a: Position, b: Position) = when {
        a.cost > b.cost -> 1
        a.cost < b.cost -> -1
        else            -> 0
    }
}