package chess

import player.Human
import player.Player
import java.lang.System.out
import player.PlayerColor.WHITE
import player.PlayerColor.BLACK

class Controller(private val light: Player, private val dark: Player, val state: State) {
    private val hasTwoHumans = light is Human && dark is Human
    private var turnCounter: Int = 0

    private var lastPlayerColor = WHITE
    private val nextPlayer get() = when(lastPlayerColor) {
        WHITE -> {
            lastPlayerColor = BLACK
            dark
        }
        BLACK -> {
            lastPlayerColor = WHITE
            light
        }
    }

    fun callNextPlayer() {
        out.println("Turn #${turnCounter++}")
        state.printBoard()
        val player = nextPlayer
        if (hasTwoHumans) out.println("$player's turn.")
        if (player.takeTurn()) out.println("$player wins!") else callNextPlayer()
    }

    companion object {
        @JvmStatic fun main(args: Array<String>) {
            val game = State()
            val (arg0, arg1) = if (args.size == 2) args.map { it.toInt() } else listOf(5, 8)
            Controller(Player(game, WHITE, arg0), Player(game, BLACK, arg1), game).callNextPlayer()
        }
    }
}