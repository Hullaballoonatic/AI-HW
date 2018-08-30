package app.functions

import app.Main.Companion.viz
import app.extensions.coordinatesOutput
import app.extensions.isValid
import app.game.GameState
import app.game.GameStateComparator
import java.io.PrintWriter
import java.util.LinkedList
import java.util.Queue
import java.util.TreeSet

val test = PrintWriter("test.txt")
fun bfs(root: ByteArray): GameState? {
    val q: Queue<GameState> = LinkedList(listOf(GameState(root)))
    val visited: MutableSet<ByteArray> = TreeSet(GameStateComparator)
    visited += root

    while (q.isNotEmpty()) {
        val node = q.remove()
        test.println("Pop: ${node.positions.coordinatesOutput}")
        System.out.println("popped")
        viz.setState(node.positions)
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
