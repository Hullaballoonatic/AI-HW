package model

import javafx.beans.property.SimpleObjectProperty
import tornadofx.*

abstract class Position(var x: Int, var y: Int, owner: Player = Player.NONE) {
    private val ownerProperty = SimpleObjectProperty(owner)
    var owner: Player by ownerProperty

    val label get() = owner.label

    val color get() = owner.color

    abstract val isActionable: Boolean
}