package app

internal class GameStatePositionComparator : Comparator<GameState> {
    override fun compare(a: GameState, b: GameState) = when {
        a.distance > b.distance -> 1
        a.distance < b.distance -> -1
        else -> 0
    }
}