package app

import java.util.PriorityQueue
import java.util.TreeSet
import java.lang.System.out

internal class Pather {
    val frontier: PriorityQueue<GameState> = PriorityQueue(GameStateCostComparator())
    private val visited = TreeSet(GameStatePositionComparator())

    fun bfs(start: GameState, stop: GameState): GameState {
        out.println("starting search")
        start.reset()
        frontier += start
        visited += start

        while (frontier.isNotEmpty) {
            val node = frontier.remove()
            out.println("curNode = " + node.toString())
            if (node.x == stop.x && node.y == stop.y) {
                out.println("found node")
                return node
            }
            node.children.forEach { child ->
                if (child in visited) {
                    val existingNode = visited.floor(child)
                    if (node.cost + child.cost < existingNode!!.cost)
                        existingNode.parent = node
                } else {
                    child.parent = node
                    frontier += child
                    visited += child
                }
            }
        }
        throw(RuntimeException("There is no path to the goal"))
    }
}