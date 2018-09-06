import java.util.*

class UniformCostSearch<D: Any, C: Any>(start: IxCostState<D, C>, private val stop: IxState<D>, dataComparator: Comparator<IxState<D>>, costComparator: Comparator<IxCostState<D, C>>) {
    private val q: MutableSet<IxCostState<D, C>> = TreeSet(costComparator)
    private val visited: MutableSet<IxState<D>> = TreeSet(dataComparator)
    init {
        q += start
    }
}