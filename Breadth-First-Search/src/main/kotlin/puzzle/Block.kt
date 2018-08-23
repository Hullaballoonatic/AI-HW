package puzzle

data class Block(val number: Byte, var coordinate: Byte, val transparentOverlay: TransparentOverlay) {
    val currentOccupiedSpaces: List<Pair<Byte, Byte>> get() = TODO("coordinate math with default occupied spaces")
}


