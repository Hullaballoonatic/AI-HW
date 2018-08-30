package app.gamestate

import app.extensions.b
import kotlinx.collections.immutable.toImmutableList
import java.util.ArrayList

class GameState(val positions: ByteArray, val parent: GameState? = null) {
    val children: List<ByteArray>
        get() {
            val result = ArrayList<ByteArray>()
            val original = positions.toList().toImmutableList()
            original.forEachIndexed { i, b ->
                result += original.set(i, (b + 1).b).toByteArray()
                result += original.set(i, (b - 1).b).toByteArray()
            }
            return result
        }

    val isGoal: Boolean get() = positions[0] == 4.b && positions[1] == (-2).b
}

