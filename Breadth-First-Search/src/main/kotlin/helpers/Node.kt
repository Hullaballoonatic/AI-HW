package helpers

abstract class Node<T: Node<T>>(val parent: T? = null, var children: List<T> = emptyList()) {
    abstract val isGoal: Boolean
    abstract val isValid: Boolean
}