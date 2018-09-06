package app.models

import app.extensions.b
import app.extensions.f
import kotlin.experimental.and

data class Position(var x: Float = 100f, var y: Float = 100f) {
    operator fun minus(pos: Position): Position = Position(this.x - pos.x, this.y - pos.y)
    operator fun timesAssign(t: Float) {
        this.x *= t
        this.y *= t
    }

    val speed: Float
        get() {
            var xx = (x * 0.1f).toInt()
            var yy = (y * 0.1f).toInt()
            if (xx >= 60) {
                xx = 119 - xx
                yy = 59 - yy
            }
            val newPos = 4 * (60 * yy + xx)
            return Math.max(
                0.2f,
                Math.min(
                    3.5f,
                    -0.01f * (m.terrain[newPos + 1] and 0xff.b) + 0.02f * (m.terrain[newPos + 3] and 0xff.b)
                )
            )
        }

    companion object {
        lateinit var m: Model
    }
}

fun Pair<Number, Number>.toPosition(): Position = Position(this.first.f, this.second.f)