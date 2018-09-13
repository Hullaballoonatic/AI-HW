package app

import kotlin.math.sqrt

internal object AStarSearchCostComparator : Comparator<GameState> {
    override fun compare(a: GameState, b: GameState): Int {
        val aHeur = dist(a,goal!!) * lowestCost
        val bHeur = dist(b,goal!!) * lowestCost
        return when {
            a.cost + aHeur > b.cost + bHeur -> 1
            a.cost + aHeur < b.cost + bHeur -> -1
            else                                    -> 0
        }
    }

    private fun dist(a: GameState, b: GameState): Float {
        val dx = a.x - b.x
        val dy = a.y - b.y
        return sqrt(dx*dx + dy*dy)
    }

    override fun toString() = "a*"

    var goal: GameState? = null

    @JvmStatic
    var lowestCost = 0f
}