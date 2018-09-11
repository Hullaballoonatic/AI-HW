package app

import app.Model.XMAX
import app.Model.YMAX
import kotlin.math.max
import kotlin.math.min
import kotlin.math.sqrt

internal class GameState(private val m: Model, position: Pair<Number,Number> = 100f to 100f) {
    constructor(m: Model): this(m, m.x to m.y)
    var x = max(0f, min(XMAX, position.first.F))
        set(v) {
            field = max(0f, min(XMAX, v))
        }
    var y = max(0f, min(YMAX, position.second.F))
        set(v) {
            field = max(0f, min(YMAX, v))
        }
    var cost = m.getDistanceToDestination(0) / m.getTravelSpeed(x, y)

    var parent: GameState? = null
        set(v) {
            field = v
            cost += field?.cost ?: 0.0
        }

    val children: List<GameState> get() = allDirections.map { GameState(m, x + it.offX to y + it.offY) }

    val distance get() = sqrt(x * x + y * y)

    override fun toString() = String.format("(%d, %d)", x.I, y.I)

}

