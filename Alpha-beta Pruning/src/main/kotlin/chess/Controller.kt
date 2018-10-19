package chess

import agent.Agent
import java.lang.System.out

class Controller(private val light: Player, private val dark: Player, private val state: ChessState) {
    private val hasTwoHumans = light is Human && dark is Human
    private var turnCounter: Int = 0

    private var lastPlayerColor = PlayerColor.WHITE
    private val nextPlayer get() = when(lastPlayerColor) {
        PlayerColor.WHITE -> {
            lastPlayerColor = PlayerColor.BLACK
            dark
        }
        PlayerColor.BLACK -> {
            lastPlayerColor = PlayerColor.WHITE
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
            val game = ChessState()
            val (light, dark) = args.map { it.toInt() }.zip(listOf(PlayerColor.WHITE, PlayerColor.BLACK)).map {
                when (it.first) {
                    0 -> Human(game, it.second)
                    else -> Agent(game, it.second, it.first)
                }
            }
            Controller(light, dark, game).callNextPlayer()
        }
    }
}