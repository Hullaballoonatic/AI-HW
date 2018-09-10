package app

import java.util.PriorityQueue
import java.util.TreeSet

class Pather(
    private val q: PriorityQueue<Position> = PriorityQueue(PositionCostComparator())
) {
    private val visited = TreeSet(PositionComparator())

    fun bfs(start: Position, stop: Position): Position {
        start.reset()
        q += start
        visited += start

        while (q.isNotEmpty) {
            val node = q.remove()
            if (node == stop) return node
            node.children.forEach { child ->
                if (child in visited) {
                    val existingNode = visited.floor(child)
                    if (node.cost + child.cost < existingNode!!.cost)
                        existingNode.parent = node
                } else {
                    child.parent = node
                    q += child
                    visited += child
                }
            }
        }
        throw(RuntimeException("There is no path to the goal"))
    }
}
