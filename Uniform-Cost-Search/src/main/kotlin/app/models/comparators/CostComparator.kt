package app.models.comparators

import app.models.IxCostState

class CostComparator<D: Any>: Comparator<IxCostState<D, Float>> {
    override fun compare(o1: IxCostState<D, Float>, o2: IxCostState<D, Float>): Int = when {
        o1.cost > o2.cost -> 1
        o1.cost < o2.cost -> -1
        else -> 0
    }
}