package app.game

import app.extensions.b

data class BoardSpace(var col: Byte, var row: Byte) {
    constructor(coords: Pair<Int, Int>) : this(coords.first.b, coords.second.b)

    override fun toString() = "($col,$row)"
}
