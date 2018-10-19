package chess

import java.lang.Exception
import java.lang.System.out

class Human(override val game: ChessState, override val color: PlayerColor) : Player() {
    override fun takeTurn(): Boolean {
        out.println("Your move? ")
        val (xSrc, ySrc, xDst, yDst) = readLine()!!.toCharArray().map { it - 'a' }
        return try {
            game.move(xSrc, ySrc, xDst, yDst)
        } catch (_: Exception) {
            takeTurn()
        }
    }
}