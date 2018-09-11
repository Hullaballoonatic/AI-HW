package app

internal class GameStateCostComparator : Comparator<GameState> {
    override fun compare(a: GameState, b: GameState) = when {
        a.cost > b.cost -> 1
        a.cost < b.cost -> -1
        else            -> 0
    }

    override fun toString() = "ucs"
}