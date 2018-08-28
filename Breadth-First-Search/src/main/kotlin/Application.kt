import helpers.bfs
import puzzle.Block
import puzzle.GameState
import java.io.PrintWriter

class Application {
    companion object {
        private val out = PrintWriter("results.txt", "UTF-8")

        private val initialState = GameState(
                listOf(
                        Block.ONE,
                        Block.TWO,
                        Block.THREE,
                        Block.FOUR,
                        Block.FIVE,
                        Block.SIX,
                        Block.SEVEN,
                        Block.EIGHT,
                        Block.NINE,
                        Block.TEN ))

        @JvmStatic
        fun main() {
            try {
                out.print(bfs(initialState)?.outputString)
            } catch (e: NullPointerException) {
                out.print("No winning state found")
            }
        }
    }
}