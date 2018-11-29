package model.grid

import controller.*
import model.table.Pos
import model.grid.SpaceType.NONE

data class Space(val pos: Pos, var type: SpaceType = NONE, val value: Double = 0.0) {
    var action: Action? = null
    override fun toString(): String = when(type) {
        NONE -> action?.label ?: ' '
        else -> type.label
    }.toString()
}