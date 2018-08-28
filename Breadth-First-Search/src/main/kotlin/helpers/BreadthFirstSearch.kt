package helpers

import java.util.*

class BreadthFirstSearch(root: Node) {
    private val q: Queue<Node> = LinkedList(listOf(root))
    private val visited: Set<Node> = TreeSet()
    init {
        visited.plus(root)
        while (q.isNotEmpty()) {
            val node = q.remove()
            // are you the goal?
            node.children.forEach { child ->
                // isValid?
                if (!visited.contains(child)) {
                    visited.plus(child)
                    q += child
                }
                // ifNeither add to queue
            }
        }
    }
}