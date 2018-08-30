package app

import app.functions.bfs
import app.vizualization.Viz
import java.io.PrintWriter

class Main {
    companion object {
        private val results = PrintWriter("results.txt")
        private val initialState = ByteArray(22)
        val viz = Viz()

        @JvmStatic
        fun main(args: Array<String>) {
            val winner = bfs(initialState)
            if (winner != null) winner.printToFile(results) else results.println("no winner found")
        }
    }
}