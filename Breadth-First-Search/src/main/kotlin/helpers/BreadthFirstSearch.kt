package helpers

import java.util.*

fun <T : Node<T>> bfs(root: T): T? {
    val q: Queue<T> = LinkedList(listOf(root))
    val visited: Set<T> = TreeSet()
    visited.plus(root)
    while (q.isNotEmpty()) {
        val node = q.remove()
        if (node.isGoal) return node

        node.children.forEach { child ->
            if (child.isValid && child !in visited) {
                visited.plus(child)
                q += child
            }
        }
    }
    return null
}