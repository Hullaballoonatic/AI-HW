package model.ticTacToe

import model.Player

class Line(vararg positionIndices: Int) {
    private val positionIndices = positionIndices.asList()

    private val linePositions get() =
        positionIndices.map { boardPositions[it] }

    val winner: Player? get() =
        linePositions[0].owner.run {
            if (this != Player.NONE && linePositions.none { it.owner != this }) this else null
        }

    val hasWinner
        get() = winner != null

    companion object {
        lateinit var m: TicTacToe
        val boardPositions get() = m.positions
    }


}