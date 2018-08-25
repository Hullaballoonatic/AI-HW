package helpers

abstract class Node<T>(val parent: Node<T>? = null, var children: List<Node<T>> = emptyList(), var visited: Boolean) {
    val unvisitedChildren get() = children.filter { it.visited }
}