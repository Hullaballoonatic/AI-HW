package controller.ticTacToe

import controller.Agent
import model.Player

class TicTacToeAgent(override val controller: TicTacToeController, override val player: Player): Agent<TicTacToeController> {
    override fun action() {
        if(controller.curPlayer == player) {
            //TODO: Determine action using Minimax

            controller.nextPlayer()
        }
    }
}