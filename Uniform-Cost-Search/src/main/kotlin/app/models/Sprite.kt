package app.models

import app.extensions.d
import app.extensions.f

class Sprite(var pos: Position = Position()) {
    var destination: Position = pos

    val distanceToDestination get() = Math.sqrt(Math.pow((pos.x - destination.x).toDouble(), 2.0) + Math.pow((pos.y - destination.y).toDouble(), 2.0))

    fun update() {
        val dPos = destination - pos
        val dist = Math.sqrt((dPos.x * dPos.x + dPos.y * dPos.y).d).f
        val t = pos.speed / Math.max(pos.speed, dist)
        dPos *= t
        pos.x += dPos.x
        pos.y += dPos.y
        pos.x = Math.max(0.0f, Math.min(X_MAX, pos.x))
        pos.y = Math.max(0.0f, Math.min(Y_MAX, pos.y))
    }
}