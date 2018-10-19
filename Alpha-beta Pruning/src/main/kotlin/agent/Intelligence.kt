package agent

import chess.ChessState
import chess.ChessState.Companion.MAX_PIECE_MOVES
import chess.PlayerColor
import chess.PlayerColor.WHITE
import kotlin.math.max
import kotlin.math.min

class Intelligence(private val state: ChessState, playerColor: PlayerColor, private val intelligenceLevel: Int = MAX_PIECE_MOVES) {
    private val isWhite = playerColor == WHITE

    fun decide(): ChessState.ChessMove = state.iterator(isWhite).run {
        var bestMove: ChessState.ChessMove? = null
        var bestScore: Int = if (isWhite) MIN else MAX
        while (hasNext()) {
            val move = next()
            val score = prune(ChessState(state, move))
            if ((isWhite && score > bestScore) || (!isWhite && score < bestScore)) {
                bestScore = score
                bestMove = move
            }
        }
        bestMove ?: throw Throwable("no viable moves")
    }

    private fun prune(state: ChessState, depth: Int = 0, isMaximizing: Boolean = true, a: Int = MIN, b: Int = MAX): Int {
        var alpha = a
        var beta = b

        val possibleMoves = state.iterator(isWhite)

        return when {
            depth == intelligenceLevel -> state.heuristic()
            isMaximizing               -> {
                var bestValue = MIN
                while (possibleMoves.hasNext() && alpha > beta) {
                    bestValue = max(bestValue, prune(possibleMoves.nextState(), depth + 1, false, alpha, beta))
                    alpha = max(alpha, bestValue)
                }
                bestValue
            }
            else                       -> {
                var bestValue = MAX
                while (possibleMoves.hasNext() && beta > alpha) {
                    bestValue = min(bestValue, prune(possibleMoves.nextState(), depth + 1, true, alpha, beta))
                    beta = min(beta, bestValue)
                }
                bestValue
            }
        }
    }

    companion object {
        const val MIN = -1000
        const val MAX = 1000
    }
}