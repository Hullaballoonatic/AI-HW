package model.ticTacToe

import model.Board
import model.Player

class TicTacToe private constructor(): Board {
    override val positions = listOf(
        Position(0, 0), Position(0, 1), Position(0, 2),
        Position(1, 0), Position(1, 1), Position(1, 2),
        Position(2, 0), Position(2, 1), Position(2, 2))

    override
    val winner: Player?
        get() = winningLine?.winner

    var winningLine: Line? = null
        get() = field ?: winnableLines.firstOrNull { it.hasWinner }

    private val winnableLines = listOf(
        Line(0, 1, 2), // horizontal top
        Line(3, 4, 5), // horizontal mid
        Line(6, 7, 8), // horizontal bot
        Line(0, 3, 6), // vertical left
        Line(1, 4, 7), // vertical mid
        Line(2, 5, 8), // vertical right
        Line(0, 4, 8), // backward diagonal
        Line(2, 4, 6)  // forward diagonal
    )

    companion object {
        operator fun invoke() = TicTacToe().apply { Line.m = this }
    }
}