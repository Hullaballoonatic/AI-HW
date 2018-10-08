package model.ticTacToe

import model.Board
import model.Player
import model.Position
import java.lang.System.out

class TicTacToe (override val positions: List<Position> = List(9) { Position(it+1) }, curPlayer: Player = Player.X
): Board {
    constructor(state: TicTacToe): this(state.positions.map { Position(it.number, it.owner) }, state.curPlayer)

    init {
        out.println(this.toString())
    }

    var lastPlayer: Player = Player.NONE
    var curPlayer: Player = curPlayer
        get() = if (isEnded) Player.NONE else field
        set(v) {
            lastPlayer = field
            field = v
        }

    fun placeToken(at: Position) {
        if (at.isActionable) {
            this[at]?.owner = curPlayer

            curPlayer = when (curPlayer) {
                Player.X -> Player.O
                Player.O -> Player.X
                else     -> Player.NONE
            }
        }
    }

    val score get() = when(winner) {
        Player.X -> 10
        Player.O -> -10
        else -> 0
    }

    override
    val winner: Player?
        get() = winningLine?.winner ?: if(positions.none { it.owner == Player.NONE }) Player.NONE else null

    var winningLine: Line? = null
        get() = field ?: winnableLines.firstOrNull { it.winner(positions) != null }

    val actionablePositions get() = positions.filter { it.isActionable }

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

    override fun toString() = "${positions[0]} ${positions[1]} ${positions[2]} \n" +
                              "${positions[3]} ${positions[4]} ${positions[5]} \n" +
                              "${positions[6]} ${positions[7]} ${positions[8]} \n"
}