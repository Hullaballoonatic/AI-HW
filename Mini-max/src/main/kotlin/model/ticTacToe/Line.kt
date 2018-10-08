package model.ticTacToe

import model.Player
import model.Position
import tornadofx.*

class Line(vararg positionIndices: Int) : ViewModel() {
    private val positionIndices = positionIndices.asList()

    var winner: Player? = null
    fun winner(boardPositions: List<Position>): Player? =
        positionIndices.map { boardPositions[it] }.run {
            val firstOwner = this[0].owner
            if (firstOwner != Player.NONE && none { it.owner != firstOwner }) {
                winner = firstOwner
                firstOwner
            } else null
        }
}