package model

import javafx.scene.paint.Color
import javafx.scene.paint.Color.*

enum class Player(val label: String, val color: Color) {
    X("X", RED),
    O("O", BLUE),
    NONE("", GREY);

    fun placeToken(at: Position) =
        if (at.isActionable) {
            at.owner = this
            true
        } else false
}