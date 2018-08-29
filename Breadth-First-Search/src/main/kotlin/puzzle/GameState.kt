package puzzle

import extensions.b
import helpers.Node
import puzzle.Block.Companion.BLACK_SQUARES
import java.util.*

class GameState(private val positions: ByteArray, parent: GameState? = null): Node<GameState>(parent) {
    private val self = this

    override val children = generateAllMoves()
    private fun generateAllMoves(): List<GameState> {
        val result = ArrayList<GameState>()
        for(p in 0 until positions.size) {
            var newPos = positions
            newPos[p]++
            result += GameState(positions = newPos, parent = self)

            newPos = positions
            newPos[p]--
            result += GameState(positions = newPos, parent = self)
        }
        return result
    }

    override val isGoal: Boolean get() = isValid && positions[0] == 4.b && positions[1] == (-2).b
    override val isValid: Boolean get() {
        // set current positions of blocks to this state's positions
        for(blockIndex in 0 until blocks.size) {
            val positionIndex = blockIndex*2
            blocks[blockIndex].coordinates = positions[positionIndex] to positions[positionIndex+1]
        }
        blocks.forEach { A ->
            A.currentOccupiedSpaces.forEach {
                if (it in BLACK_SQUARES) return false

                (blocks - A).forEach { B ->
                    if (it in B.currentOccupiedSpaces) return false
                }
            }
        }
        return true
    }

    override fun equals(other: Any?) = comparator.compare(this, other as GameState) != 0

    //TODO: what does this function even do, and why was i suggested to override it?
    override fun hashCode() = 31 * Arrays.hashCode(positions) + children.hashCode()

    val coordinatesOutput: String get() {
        val sb = StringBuilder()
        sb.append(java.lang.Byte.toString(positions[0]))
        for (i in 1 until positions.size) {
            sb.append(",")
            sb.append(java.lang.Byte.toString(positions[i]))
        }
        return sb.toString()
    }

    companion object {
        val comparator: GameStateComparator = GameStateComparator()
    }
}

