package agent

import chess.State
import player.PlayerColor
import player.PlayerColor.WHITE
import java.lang.RuntimeException
import java.util.Random
import kotlin.math.max
import kotlin.math.min

class Intelligence(private val state: State, private val color: PlayerColor,
                   private val intelligenceLevel: Int = MAX_PIECE_MOVES, private val rand: Random = Random()) {
    override fun toString() = "$color AI $intelligenceLevel"

    private val isWhite = color == WHITE

    private val Int.scoreFormat: String get() = when (this) {
        MIN -> "−∞"
        MAX -> "+∞"
        else -> toString()
    }

    val bestMove: State.ChessMove
        get() {
            print("${toString()} is thinking")
            val it = state.iterator(isWhite)

            var bestMove: State.ChessMove? = null
            var bestScore = if (isWhite) MIN else MAX

            //print("%3s : ".format(bestScore.scoreFormat))

            while (it.hasNext()) {
                print(".")
                val move = it.next()
                val score = prune(State(state, move), !isWhite)
                //print("%3s, ".format(score.scoreFormat))
                if (isWhite) {
                    if (score >= bestScore) {
                        //if (score > bestScore) print("\n%3s : ".format(score.scoreFormat))
                        bestMove = move
                        bestScore = score
                    }
                } else {
                    if (score <= bestScore) {
                        //if (score < bestScore) print("\n%3s : ".format(score.scoreFormat))
                        bestMove = move
                        bestScore = score
                    }
                }
            }

            println()

            return bestMove ?: throw RuntimeException("$color failed to find a viable move")
        }

    private fun prune(state: State, isMaximizing: Boolean, depth: Int = intelligenceLevel, a: Int = MIN,
                      b: Int = MAX): Int {
        //state.printBoard(System.out)

        var alpha = a
        var beta = b

        val it = state.iterator(isMaximizing)

        return when {
            depth == 0   -> state.heuristic(rand)
            isMaximizing -> {
                var value = MIN
                while (it.hasNext() && alpha < beta) {
                    value = max(value, prune(State(state, it.next()), false, depth - 1, alpha, beta))
                    alpha = max(alpha, value)
                }
                value
            }
            else         -> {
                var value = MAX
                while (it.hasNext() && alpha < beta) {
                    value = min(value, prune(State(state, it.next()), true, depth - 1, alpha, beta))
                    beta = min(beta, value)
                }
                value
            }
        }
    }

    companion object {
        const val MIN = -100000
        const val MAX = 100000

        const val MAX_PIECE_MOVES = 27
    }
}