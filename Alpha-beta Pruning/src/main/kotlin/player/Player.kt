package player

import agent.Agent
import agent.Intelligence.Companion.MAX_PIECE_MOVES
import chess.State
import kotlin.math.max
import kotlin.math.min

abstract class Player {
    abstract val game: State
    abstract val color: PlayerColor
    abstract fun takeTurn(): Boolean

    override fun toString() = color.toString()

    companion object {
        operator fun invoke(state: State, color: PlayerColor, arg: Int) =
            if (arg == 0)
                Human(state, color)
            else
                Agent(state, color, max(0, min(arg, MAX_PIECE_MOVES)))
    }
}