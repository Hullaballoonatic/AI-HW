package extensions

val Number.b get() = this.toByte()

val Pair<Int,Int>.b get() = this.first.b to this.second.b

fun bytePairListOf(vararg elements: Pair<Int,Int>) = elements.map { it.first.b to it.second.b }