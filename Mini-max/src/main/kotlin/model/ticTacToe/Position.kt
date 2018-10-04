package model.ticTacToe

import model.Player
import model.Position

class Position(x: Int, y: Int) : Position(x, y, Player.NONE) {
    override val isActionable get() = owner == Player.NONE
}