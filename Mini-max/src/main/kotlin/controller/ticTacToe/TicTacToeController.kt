package controller.ticTacToe

import javafx.beans.property.SimpleStringProperty
import model.Player
import model.ticTacToe.TicTacToe
import tornadofx.*

class TicTacToeController {
    val board = TicTacToe()
    var agent: TicTacToeAgent? = null

    var robotLock = true
    var playerLock = false

    val statusProperty = SimpleStringProperty("choose your opponent")
    var status: String by statusProperty

    fun start() {
        if (agent != null) board.placeToken(board.actionablePositions.first())
        update()
    }

    fun update() {
        status = board.winner?.let { winner ->
            when(winner) {
                Player.NONE -> "tie!"
                else        -> "${board.lastPlayer} wins!"
            }
        } ?: "${board.curPlayer}'s turn"

        if(agent != null) {
            agent?.action()
            robotLock = !robotLock
        }
    }
}