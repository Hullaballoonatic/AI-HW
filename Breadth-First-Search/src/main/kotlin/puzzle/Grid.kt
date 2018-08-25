package puzzle

import helpers.Node
import java.util.LinkedList

abstract class Grid<T, N: Node<T>>(elements: List<LinkedList<N>>) {
    abstract fun action(target: N)
}

