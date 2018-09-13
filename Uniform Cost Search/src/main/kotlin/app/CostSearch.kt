package app

import java.util.PriorityQueue
import java.util.TreeSet

internal object CostSearch {
    private val visited = TreeSet(GameStatePositionComparator)
    lateinit var q: PriorityQueue<GameState>

    fun search(start: GameState, stop: GameState, comp: Comparator<GameState>): List<GameState> {
        q = PriorityQueue(comp)
        q.add(start)
        visited.clear()
        visited += start

        while (q.isNotEmpty) {
            val node = q.remove()
            if (node == stop) return node.familyTree
            node.children.forEach { child ->
                if (child in visited) {
                    val existingNode = visited.floor(child)
                    if (node.cost + child.cost < existingNode!!.cost)
                        existingNode.parent = node
                } else {
                    child.parent = node
                    q.add(child)
                    visited += child
                }
            }
        }
        throw(RuntimeException("There is no path to the goal"))
    }
}