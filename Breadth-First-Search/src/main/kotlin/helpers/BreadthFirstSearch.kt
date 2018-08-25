package helpers

import puzzle.Grid
import java.util.LinkedList
import java.util.Queue

class BreadthFirstSearch<T>(root: Node<T>, graph: Grid<T, Node<T>>) {
    private val q: Queue<Node<T>> = LinkedList(listOf(root))
    init {
        root.visited = true
        graph.action(root)
        while (q.isNotEmpty()) {
            val node = q.remove()
            node.unvisitedChildren.forEach { child ->
                child.visited = true
                graph.action(child)
                q += child
            }
        }
    }
}