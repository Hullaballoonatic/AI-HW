package app

import app.generic.Node
import kotlin.math.max
import kotlin.math.min
import kotlin.math.sqrt

class Position(x: Number = 100f, y: Number = 100f) : Node<Pair<Float, Float>, Double> {
    override var parent: Node<Pair<Float, Float>, Double>? = null
        set(value) {
            field = value
            if (value != null) cost += value.cost
        }

    override val children get() = allDirections.map { it.offset + this }
    override var data: Pair<Float, Float>
        get() = x to y
        set(value) {
            x = value.first
            y = value.second
        }

    var x = x.F
        set(value) {
            field = max(0f, min(X_MAX, value))
        }
    var y = y.F
        set(value) {
            field = max(0f, min(Y_MAX, value))
        }

    override var cost = 0.0

    override fun reset(): Node<Pair<Float, Float>, Double> {
        parent = null
        cost = 0.0
        return this
    }

    val distance get() = sqrt(x * x + y * y)
    fun distanceTo(to: Position) = sqrt((x - to.x) * (x - to.x) + (y - to.y) * (y - to.y))

    operator fun minus(pos: Position) = Position(x - pos.x, y - pos.y)
    operator fun minusAssign(pos: Position) {
        x -= pos.x
        y -= pos.y
    }

    operator fun plus(pos: Position) = Position(x + pos.x, y + pos.y)
    operator fun plusAssign(pos: Position) {
        x += pos.x
        y += pos.y
    }

    operator fun times(t: Number) = Position(x * t.F, y * t.F)
    operator fun timesAssign(t: Number) {
        x *= t.F
        y *= t.F
    }

    operator fun times(p: Position) = Position(x * p.x, y * p.y)
    operator fun timesAssign(p: Position) {
        x *= p.x
        y *= p.y
    }

    operator fun div(d: Number) = Position(x / d.F, y / d.F)
    operator fun divAssign(d: Number) {
        x /= d.F
        y /= d.F
    }

    operator fun div(p: Position) = Position(x / p.x, y / p.y)
    operator fun divAssign(p: Position) {
        x /= p.x
        y /= p.y
    }

    companion object {
        private const val EPSILON = 0.0001f // A small number
        private const val X_MAX = 1200.0f - EPSILON // The maximum horizontal screen position. (The minimum is 0.)
        private const val Y_MAX = 600.0f - EPSILON // The maximum vertical screen position. (The minimum is 0.)
    }
}