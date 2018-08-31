package app

import java.io.PrintWriter

class Main {
    companion object {
        private val results = PrintWriter("results.txt")
        private val initialState = ByteArray(22)

        @JvmStatic
        fun main(args: Array<String>) {
            results.print("test")
            //bfs(initialState)!!.printToFile(results)
        }
    }
}