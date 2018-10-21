package player

import chess.State
import java.lang.Exception
import player.PlayerColor.WHITE

class Human(override val game: State, override val color: PlayerColor) : Player() {
    override fun takeTurn(): Boolean = try {
        println("Your move? ")

        val entry = readLine()!!

        if (entry == "q") System.exit(0)

        val (colSource, rowSource, colDestination, rowDestination) = entry.map {
            if (it.isLetter()) it.toLowerCase() - 'a'
            else               it - '0' - 1
        }
        when {
            !game.isWhite(colSource, rowSource) == (color == WHITE)                -> {
                println("You cannot move a piece that isn't $color")
                takeTurn()
            }
            game.isValidMove(colSource, rowSource, colDestination, rowDestination) ->
                game.move(colSource, rowSource, colDestination, rowDestination)
            else                                                                   -> {
                println("invalid move")
                takeTurn()
            }
        }
    } catch (_: Exception) {
        println("choose move by location of piece to move followed by location destination, e.g. a2a4")
        takeTurn()
    }
}