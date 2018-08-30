package app.game

import app.extensions.b

data class BoardSpace(var col: Byte, var row: Byte) {
    constructor(coords: Pair<Int, Int>) : this(coords.first.b, coords.second.b)
}

fun boardSpaceListOf(vararg coords: Pair<Int, Int>) = coords.map { coord -> BoardSpace(coord) }