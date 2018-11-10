package model

class InteriorNode(val a: Node, val b: Node, val attributeIndex: Int, val pivot: Double, val type: ValueType) : Node {
    override fun toString(): String {
        return "( c: $attributeIndex, p: $pivot, t: $type )"
    }
}