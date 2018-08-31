package app

import app.functions.bfs
import java.io.PrintWriter

class Main {
    companion object {
        private val results = PrintWriter("results.txt")
        private val initialState = ByteArray(22)

        @JvmStatic
        fun main(args: Array<String>) {
            bfs(initialState)!!.printToFile(PrintWriter(results))
        }
    }
}