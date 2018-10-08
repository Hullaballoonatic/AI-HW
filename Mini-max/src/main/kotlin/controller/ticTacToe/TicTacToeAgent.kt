package controller.ticTacToe

import model.Player

class TicTacToeAgent(private val controller: TicTacToeController) {
    private val game = controller.board

    fun action() {
        if (game.curPlayer == Player.X && !controller.robotLock) {
            controller.playerLock = true

            game.placeToken(Minimax(game).execute())

            controller.playerLock = false

            controller.update()
        }
    }
}