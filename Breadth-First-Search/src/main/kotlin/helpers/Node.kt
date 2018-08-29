package helpers

abstract class Node<T: Node<T>>(val parent: T? = null) {
    abstract val isGoal: Boolean
    abstract val isValid: Boolean
    abstract val children: List<T>
}