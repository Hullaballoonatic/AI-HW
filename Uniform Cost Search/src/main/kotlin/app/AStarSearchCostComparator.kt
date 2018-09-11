package app

internal class AStarSearchCostComparator : Comparator<GameState> {
    override fun compare(a: GameState, b: GameState) = when {
        a.cost + heuristic > b.cost + heuristic -> 1
        a.cost + heuristic < b.cost + heuristic -> -1
        else            -> 0
    }

    companion object {
        var heuristic: Double = 0.1
    }
}