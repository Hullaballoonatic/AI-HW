package controller.ticTacToe

import controller.Agent
import javafx.beans.property.SimpleObjectProperty
import model.Player
import model.ticTacToe.TicTacToe
import tornadofx.*
import java.lang.System.out

class TicTacToeController : Controller() {
    val board = TicTacToe()
    var curPlayer: Player = Player.X

    private val agentProperty = SimpleObjectProperty<Agent<TicTacToeController>?>(null)
    var agent by agentProperty

    fun startGame(numPlayers: Int = 2) { //TODO: add ability to change number of users
        when {
            numPlayers > 2 -> throw(Exception("too many players"))
            numPlayers < 1 -> throw(Exception("too few players"))
            numPlayers == 1 -> agent = TicTacToeAgent(this, Player.O)
        }
        nextPlayer()
    }

    fun nextPlayer() {
        board.winner?.let {
            out.println("winner = $curPlayer") //TODO: highlight winning line
            curPlayer = Player.NONE
        }

        curPlayer = when(curPlayer) {
            Player.X -> Player.O
            Player.O -> Player.X
            else     -> Player.NONE
        }

        agent?.action()
    }
}