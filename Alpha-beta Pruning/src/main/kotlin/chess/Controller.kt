package chess

import player.Player
import player.PlayerColor.WHITE
import player.PlayerColor.BLACK

class Controller(private val light: Player, private val dark: Player, val state: State) {

    private var lastPlayerColor = BLACK
    private val nextPlayer get() = when(lastPlayerColor) {
        WHITE -> dark.also { lastPlayerColor = BLACK }
        BLACK -> light.also { lastPlayerColor = WHITE }
    }

    fun callNextPlayer() {
        println("\n")
        state.printBoard(System.out)

        nextPlayer.run {
            if (takeTurn())
                println("$this wins!")
            else callNextPlayer()
        }
    }

    companion object {
        @JvmStatic fun main(args: Array<String>) {
            val game = State()
            val (depthLight, depthDark) = if (args.size == 2) args.map { it.toInt() } else listOf(3, 1)

            Controller(
                light = Player(game, WHITE, depthLight),
                dark =  Player(game, BLACK, depthDark),
                state = game
            ).callNextPlayer()
        }
    }
}