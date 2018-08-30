package app.specific

import app.extensions.b
import app.extensions.toPairList
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

    val isGoal: Boolean get() = positions[0] == 4.b && positions[-1] == (-2).b
}

val ByteArray.coordinatesOutput: String
    get() {
        val sb = StringBuilder()
        this.toPairList.forEach {
            sb.append("(" + it.first + "," + it.second + ") ")
        }
        return sb.trim().toString()
    }

//TODO: broken. states conflicting with themselves
val ByteArray.isValid: Boolean
    get() {
        val coordinates = this.toPairList
        val occupiedSpaces = Block.BLACK_SQUARES as ArrayList<Pair<Byte, Byte>>
        blocks.forEachIndexed { index, block ->
            block.coordinates = coordinates[index]
            block.currentOccupiedSpaces.forEach { space ->
                if (space in occupiedSpaces) {
                    System.out.println(this.coordinatesOutput + ": Block #" + block.label + " conflicts on space: (" + space.first + "," + space.second + ")")
                    return false
                } else {
                    System.out.println("adding occupied space: (" + space.first + "," + space.second + ")")
                    occupiedSpaces += space
                }
            }
        }
        System.out.println("child valid")
        return true
    }

