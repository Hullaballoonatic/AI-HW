package app.generic

import app.specific.GameState
import app.specific.GameStateComparator
import app.specific.isValid
import java.util.LinkedList
import java.util.Queue
import java.util.TreeSet

fun bfs(root: GameState): GameState? {
    System.out.println("Starting bfs")
    val q: Queue<GameState> = LinkedList(listOf(root))
    val visited: MutableSet<ByteArray> = TreeSet(GameStateComparator)
    visited += root.positions

    while (q.isNotEmpty()) {
        val node = q.remove()
        if (node.isGoal) return node
        node.children.forEach { child ->
            if (child.isValid && child !in visited) {
                System.out.println("adding child")
                visited += child
                q += GameState(positions = child, parent = node)
            }
        }
    }
    return null
}
