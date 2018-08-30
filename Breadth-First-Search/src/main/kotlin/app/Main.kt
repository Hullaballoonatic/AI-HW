package app

import app.extensions.coordinatesOutput
import app.functions.bfs
import app.gamestate.GameState
import java.io.PrintWriter
import java.util.LinkedList

val results = PrintWriter("results.txt")
class Main {
    companion object {
        private val initialState = GameState(ByteArray(22))

        @JvmStatic
        fun main(args: Array<String>) {
            val winner = bfs(initialState)
            if (winner == null) {
                results.print("No winning state found")
            } else {
                System.out.println("found a winner")
                val allMoves = LinkedList<ByteArray>()
                var curState = winner
                while (curState?.parent != null) {
                    System.out.println("has a parent")
                    allMoves += curState.positions
                    curState = curState.parent
                }
                System.out.println("printing results")
                allMoves.asReversed().forEach {
                    results.println(it.coordinatesOutput)
                }
            }
        }
    }
}