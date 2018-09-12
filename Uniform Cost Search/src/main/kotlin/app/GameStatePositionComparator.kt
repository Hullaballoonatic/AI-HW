package app

internal object GameStatePositionComparator : Comparator<GameState> {
    override fun compare(a: GameState, b: GameState): Int {
        val ax = a.x.truncate
        val bx = b.x.truncate
        val ay = a.y.truncate
        val by = b.y.truncate
        return when {
            ax < bx -> -1
            ax > bx -> 1
            ay < by -> -1
            ay > by -> 1
            else -> 0
        }
    }
}