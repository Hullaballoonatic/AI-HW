package app

import app.Model.XMAX
import app.Model.YMAX
import kotlin.experimental.and
import kotlin.math.max
import kotlin.math.min
import kotlin.math.sqrt

internal class GameState (x: Float = 100f, y: Float = 100f, var cost: Double = 0.0, parent: GameState? = null) {
    private val self = this
    var x = max(0f, min(XMAX, x))
        set(v) {
            field = max(0f, min(XMAX, v))
        }
    var y = max(0f, min(YMAX, y))
        set(v) {
            field = max(0f, min(YMAX, v))
        }
    var parent = parent
        set(v) {
            field = v
            cost += field?.cost ?: 0.0
        }

    val children: List<GameState>

    private var speed: Float
    init {
        var xx = (x * 0.1f).I
        var yy = (y * 0.1f).I
        if (xx >= 60) {
            xx = 119 - xx
            yy = 59 - yy
        }
        val pos = 4 * (60 * yy + xx)
        speed = Math.max(0.2f, Math.min(3.5f, -0.01f * (terrain[pos + 1] and 0xff.B) + 0.02f * (terrain[pos + 3] and 0xff.B))).F

        val childPos = allDirections.map { x + it.offX to y + it.offY }
        val childCost = childPos.map { (distanceTo(it)/speed).D }
        children = List(8) { GameState(childPos[it].first, childPos[it].second, childCost[it], self) }
    }


    val distance get() = sqrt(x * x + y * y)
    private fun distanceTo(to: Pair<Float,Float>): Float {
        val dx = x - to.first
        val dy = y - to.second
        return sqrt(dx*dx + dy*dy)
    }

    fun reset() {
        this.parent = null
        this.cost = 0.0
    }

    override fun toString() = String.format("(%d, %d)\n", x.I, y.I)

    companion object {
        lateinit var terrain: ByteArray
    }
}

