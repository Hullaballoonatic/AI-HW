package app.generic

interface Node<Data: Any, Cost: Comparable<*>> {
    var parent: Node<Data, Cost>?
    val children: List<Node<Data, Cost>>
    var data: Data
    var cost: Cost

    fun reset(): Node<Data, Cost>
}