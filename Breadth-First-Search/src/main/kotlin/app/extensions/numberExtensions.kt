package app.extensions

val Number.b get() = this.toByte()

val Pair<Int,Int>.b get() = this.first.b to this.second.b

fun bytePairListOf(vararg elements: Pair<Int,Int>) = elements.map { it.first.b to it.second.b }

val ByteArray.toPairList: List<Pair<Byte,Byte>> get() {
    val result = ArrayList<Pair<Byte,Byte>>()
    for (i in 0 until this.size step 2) {
        result += this[i] to this[i + 1]
    }
    return result
}