package controller.ticTacToe

import model.Player
import model.ticTacToe.TicTacToe
import java.lang.System.out

class Minimax(private val state: TicTacToe) {
    fun execute() = possibleMoves.maxBy { it.first.apply{out.println(this.toString())} }?.second ?: throw(Exception("No viable moves"))

    private val possibleMoves = state.actionablePositions.map {
        minimax(TicTacToe(state).apply { placeToken(it) }) to it
    }

    private fun minimax(state: TicTacToe): Int {
        if (state.isEnded) return state.score

        val scores = state.actionablePositions.map { minimax(TicTacToe(state).apply { placeToken(it) }) }

        return when(state.curPlayer) {
            Player.X -> scores.max()!!
            Player.O -> scores.min()!!
            else -> 0
        }
    }
}