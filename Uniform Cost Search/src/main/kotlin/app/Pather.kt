package app

import java.util.PriorityQueue
import java.util.TreeSet

internal class Pather {
    private val visited = TreeSet(GameStatePositionComparator())
    lateinit var frontier: PriorityQueue<GameState>

    fun search(start: GameState, stop: Pair<Int,Int>, comp: Comparator<GameState>): List<GameState> {
        frontier = PriorityQueue(comp)
        frontier.add(start)
        visited.clear()
        visited += start

        while (frontier.isNotEmpty) {
            val node = frontier.remove()
            if ((node.x <= (stop.first + 5) && node.x >= (stop.first - 5)) && (node.y <= (stop.second + 5) && node.y >= (stop.second - 5))) {
                val family = arrayListOf(node)
                var member = node.parent
                while (member?.parent != null) {
                    family += member
                    member = member.parent
                }
                return family.reversed()
            }
            node.children.forEach { child ->
                if (child in visited) {
                    val existingNode = visited.floor(child)
                    if (node.cost + child.cost < existingNode!!.cost)
                        existingNode.parent = node
                } else {
                    child.parent = node
                    frontier.add(child)
                    visited += child
                }
            }
        }
        throw(RuntimeException("There is no path to the goal"))
    }
}