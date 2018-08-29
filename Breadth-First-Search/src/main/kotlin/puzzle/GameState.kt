package puzzle

import extensions.b
import helpers.Node
import java.util.*



/**
 * the children of these nodes are previous game states
 */
data class GameState(val blocks: List<Block>): Node<GameState>() {
    val state: List<Pair<Byte, Byte>> = blocks.map { it.coordinates }

    override val isGoal: Boolean get() = state[0] == (4 to -2).b
    override val isValid: Boolean get() {
        val checked: Queue<Block> = LinkedList<Block>()
        blocks.forEach { a ->
            a.currentOccupiedSpaces.forEach {
                if (it in Block.BLACK_SQUARES) {
                    return false
                }
            }
            checked.add(a)
            blocks.forEach { b ->
                b.currentOccupiedSpaces.forEach {
                    if (it in a.currentOccupiedSpaces) {
                        return false
                    }
                }
            }
        }
        return true
    }

    override fun equals(other: Any?) = comparator.compare(this, other as GameState) != 0
    override fun hashCode() = 31 * blocks.hashCode() + state.hashCode()

    val outputString: String get() {
        val resultBuilder = StringBuilder()
        (children + this).forEach {
            val sb = StringBuilder()
            val formatter = Formatter(sb)
            it.blocks.forEach { block ->
                formatter.format(coordinatesFormat, block.coordinates.first, block.coordinates.second)
            }
            resultBuilder.append(sb.trim().toString() + "\n")
        }
        return resultBuilder.toString()
    }

    companion object {
        const val coordinatesFormat = "(%d, %d) "
        val comparator: GameStateComparator = GameStateComparator()
    }
}

