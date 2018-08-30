package app.functions

import app.extensions.coordinatesOutput
import app.extensions.isValid
import app.gamestate.GameState
import app.gamestate.GameStateComparator
import java.io.PrintWriter
import java.util.LinkedList
import java.util.Queue
import java.util.TreeSet

val test = PrintWriter("test.txt")
fun bfs(root: GameState): GameState? {
    val q: Queue<GameState> = LinkedList(listOf(root))
    val visited: MutableSet<ByteArray> = TreeSet(GameStateComparator)
    visited += root.positions

    while (q.isNotEmpty()) {
        val node = q.remove()
        val p = node.positions.coordinatesOutput
        test.print("\nPop: $p")
        System.out.print("\nPop: $p")
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
