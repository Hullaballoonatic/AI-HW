package player

import chess.State
import java.lang.Exception
import java.lang.System.out

class Human(override val game: State, override val color: PlayerColor) : Player() {
    override fun takeTurn(): Boolean {
        out.println("Your move? ")
        val (colSource, rowSource, colDestination, rowDestination) = readLine()!!.toCharArray().map { it - 'a' }
        return try {
            game.move(colSource, rowSource, colDestination, rowDestination)
        } catch (_: Exception) {
            takeTurn()
        }
    }
}