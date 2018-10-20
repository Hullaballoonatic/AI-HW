package agent

import chess.State
import player.PlayerColor
import player.PlayerColor.WHITE
import kotlin.math.max
import kotlin.math.min

class Intelligence(private val state: State, private val playerColor: PlayerColor, private val intelligenceLevel: Int = MAX_PIECE_MOVES) {
    private val isWhite = playerColor == WHITE

    fun decide(): State.Move = state.iterator(playerColor).run {
        var bestMove: State.Move? = null
        var bestScore: Int = if (playerColor == WHITE) MIN else MAX
        while (hasNext()) {
            val move = next()
            val score = prune(State(state, move))
            if ((isWhite && score > bestScore) || (!isWhite && score < bestScore)) {
                bestScore = score
                bestMove = move
            }
        }
        bestMove ?: throw Throwable("no viable moves")
    }

    private fun prune(state: State, depth: Int = 0, isMaximizing: Boolean = true, a: Int = MIN, b: Int = MAX): Int {
        var alpha = a
        var beta = b

        val possibleMoves = state.iterator(playerColor)

        return when {
            depth == intelligenceLevel -> state.heuristic()
            isMaximizing               -> {
                var bestValue = MIN
                while (possibleMoves.hasNext() && alpha > beta) {
                    bestValue = max(bestValue, prune(State(state, possibleMoves.next()), depth + 1, false, alpha, beta))
                    alpha = max(alpha, bestValue)
                }
                bestValue
            }
            else                       -> {
                var bestValue = MAX
                while (possibleMoves.hasNext() && beta > alpha) {
                    bestValue = min(bestValue, prune(State(state, possibleMoves.next()), depth + 1, true, alpha, beta))
                    beta = min(beta, bestValue)
                }
                bestValue
            }
        }
    }

    companion object {
        const val MIN = -1000
        const val MAX = 1000

        const val MAX_PIECE_MOVES = 27
    }
}