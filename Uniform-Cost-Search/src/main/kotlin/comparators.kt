class CoordinatesComparator: Comparator<State<Pair<Int, Int>>> {
    override fun compare(o1: State<Pair<Int, Int>>, o2: State<Pair<Int, Int>>): Int =
        if (o1.data.first == o2.data.first && o1.data.second == o2.data.second) 0 else 1
}

class CostComparator: Comparator<CostState<Any, Float>> {
    override fun compare(o1: CostState<Any, Float>, o2: CostState<Any, Float>): Int = when {
        o1.cost > o2.cost -> 1
        o1.cost < o2.cost -> -1
        else -> 0
    }
}