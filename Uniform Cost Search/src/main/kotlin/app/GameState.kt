package app

import app.Model.X_MAX
import app.Model.Y_MAX
import java.awt.event.MouseEvent
import java.lang.System.out
import kotlin.math.max
import kotlin.math.min
import kotlin.math.pow
import kotlin.math.sqrt

internal class GameState(private val m: Model, position: Pair<Number,Number> = 100f to 100f) {
    constructor(m: Model, e: MouseEvent): this(m, e.x to e.y)
    constructor(m: Model): this(m, m.x to m.y)

    var x = max(0f, min(X_MAX, position.first.toFloat()))
        set(v) {
            field = max(0f, min(X_MAX, v))
        }
    var y = max(0f, min(Y_MAX, position.second.toFloat()))
        set(v) {
            field = max(0f, min(Y_MAX, v))
        }

    var parent: GameState? = null
        set(v) {
            field = v
            cost += field?.cost ?: 0.0
        }

    val children: List<GameState> get() = allDirections.map { GameState(m, x + it.offX to y + it.offY) }

    var cost = m.getDistanceToDestination(0) / m.getTravelSpeed(x, y)

    override fun toString() = String.format("(%d, %d)", x.toInt(), y.toInt())

    override fun equals(other: Any?): Boolean = other is GameState && x.truncate == other.x.truncate && y.truncate == other.y.truncate
    override fun hashCode(): Int {
        var result = m.hashCode()
        result = 31 * result + x.hashCode()
        result = 31 * result + y.hashCode()
        return result
    }

    val familyTree: List<GameState> get() {
        val family = arrayListOf(this)
        var member = this
        while(member.parent != null) {
            out.println(member.toString())
            member = member.parent!!
            family += member
        }
        return family.reversed()
    }
}