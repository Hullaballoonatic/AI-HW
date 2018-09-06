package app.controllers.commands

import app.models.IxCostState
import java.util.Comparator
import java.util.TreeSet

class UniformCostSearch<D: Any, C: Any>(dataComparator: Comparator<IxCostState<D, C>>, costComparator: Comparator<IxCostState<D, C>>) {
    private val q: TreeSet<IxCostState<D, C>> = TreeSet(costComparator)
    private val visited: MutableSet<IxCostState<D, C>> = TreeSet(dataComparator)

    fun execute(start: IxCostState<D,C>, stop: IxCostState<D, C>) {
        q.clear()
        visited.clear()
        q += start

        while(q.isNotEmpty()) {
            val curNode = q.pollLast()
            if (curNode == stop) return TODO()
            TODO("generate children")
        }
    }
}