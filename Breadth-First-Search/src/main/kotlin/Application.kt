import helpers.bfs
import puzzle.GameState
import java.io.PrintWriter
import java.util.*

class Application {
    companion object {
        private val out = PrintWriter("results.txt", "UTF-8")

        private val initialState = GameState(ByteArray(22))

        @JvmStatic
        fun main() {
            val winner = bfs(initialState)
            if (winner == null) {
                out.print("No winning state found")
            } else {
                val allMoves = LinkedList<GameState>()
                var curState = winner
                while (curState?.parent != null) {
                    allMoves.add(curState)
                    curState = curState.parent
                }
                allMoves.asReversed().forEach {
                    out.println(it.coordinatesOutput)
                }
            }
        }
    }
}