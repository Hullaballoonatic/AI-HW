package app.functions

import app.game.BLACK_SQUARES
import app.game.BoardSpace
import app.game.GameState
import app.game.GameStateComparator
import app.game.allPieces
import app.vizualization.Viz
import java.util.LinkedList
import java.util.Queue
import java.util.TreeSet

fun bfs(root: ByteArray): GameState? {
    val q: Queue<GameState> = LinkedList(listOf(GameState(root)))
    val visited: MutableSet<ByteArray> = TreeSet(GameStateComparator)
    visited += root

    while (q.isNotEmpty()) {
        val node = q.remove()
        Viz.setState(node.positions)

        if (node.isGoal) return node
        node.children.forEach { child ->
            if (child.isValid && child !in visited) {
                visited += child
                q += GameState(positions = child, parent = node)
            }
        }
    }
    return null
}

val ByteArray.isValid: Boolean
    get() {
        val occupied = ArrayList<BoardSpace>(BLACK_SQUARES)
        for(i in allPieces.indices) {
            allPieces[i].getOccupiedSpaces(this[i * 2], this[i * 2 + 1]).forEach {
                if (it in occupied) return false else occupied += it
            }
        }
        return true
    }
