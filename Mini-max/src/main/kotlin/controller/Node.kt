package controller

interface Node<E, F: Node<E, F>> {
    var parent: F?
    val state: E
    var maximizingUtility: Double
    var minimizingUtility: Double
}