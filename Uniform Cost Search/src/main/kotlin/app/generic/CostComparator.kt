package app.generic

open class CostComparator<N: Node<*,Comparable<Any>>> : Comparator<N> {
    override fun compare(a: N, b: N) = when {
        a.cost > b.cost -> 1
        a.cost < b.cost -> -1
        else            -> 0
    }
}