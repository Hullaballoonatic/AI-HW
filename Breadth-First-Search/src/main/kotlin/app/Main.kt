package app

import app.functions.bfs
import java.io.PrintWriter

class Main {
    companion object {
        private val results = PrintWriter("results.txt")
        private val initialState = ByteArray(22)

        @JvmStatic
        fun main(args: Array<String>) {
            val winner = bfs(initialState)!!
            results.print(winner)
            System.out.print(winner)
        }
    }
}