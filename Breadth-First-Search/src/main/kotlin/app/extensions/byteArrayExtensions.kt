package app.extensions

fun ByteArray.replacedElement(index: Int, value: Byte): ByteArray = try {
    val result = this
    result[index] = value
    result
} catch (e: ArrayIndexOutOfBoundsException) {
    this
}