package app

import app.generic.bfs
import app.specific.GameState
import app.specific.coordinatesOutput
import java.io.PrintWriter
import java.util.LinkedList

class Main {
    companion object {
        private val out = PrintWriter("results.txt", "UTF-8")

        private val initialState = GameState(ByteArray(22))

        @JvmStatic
        fun main(args: Array<String>) {
            val winner = bfs(initialState)
            if (winner == null) {
                System.out.println("found nothing")
                out.print("No winning state found")
            } else {
                System.out.println("found a winner")
                val allMoves = LinkedList<ByteArray>()
                var curState = winner
                while (curState?.parent != null) {
                    allMoves += curState.positions
                    curState = curState.parent
                }
                allMoves.asReversed().forEach {
                    out.println(it.coordinatesOutput)
                }
            }
        }
    }
}