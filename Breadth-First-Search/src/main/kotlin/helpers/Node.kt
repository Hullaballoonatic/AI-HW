package helpers

abstract class Node(val parent: Node? = null, var children: List<Node> = emptyList()) { }