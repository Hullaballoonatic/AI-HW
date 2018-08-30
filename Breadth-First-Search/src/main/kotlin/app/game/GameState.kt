package app.game

import app.extensions.b
import app.extensions.coordinatesOutput
import kotlinx.collections.immutable.toImmutableList
import java.io.PrintWriter
import java.util.ArrayList

class GameState(val positions: ByteArray, val parent: GameState? = null) {
    val children: List<ByteArray>
        get() {
            val result = ArrayList<ByteArray>()
            val original = positions.toList().toImmutableList()
            original.forEachIndexed { i, byte ->
                result += original.set(i, (byte + 1).b).toByteArray()
                result += original.set(i, (byte - 1).b).toByteArray()
            }
            return result
        }

    val isGoal: Boolean get() = positions[0] == 4.b && positions[1] == (-2).b

    fun printToFile(file: PrintWriter) {
        if (parent != null) {
            parent.printToFile(file)
        } else {
            file.println(positions.coordinatesOutput)
        }
    }
}
