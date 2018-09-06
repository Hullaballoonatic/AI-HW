interface IxState<D: Any> {
    var parent: IxState<D>
    var data: D
}

interface IxCostState<D: Any, C: Any>: IxState<D> {
    override var parent: IxState<D>
    override var data: D
    var cost: C
}

data class State<D: Any>(override var parent: IxState<D>, override var data: D): IxState<D>

data class CostState<D: Any, C: Any>(override var parent: IxState<D>, override var data: D, override var cost: C): IxCostState<D, C>