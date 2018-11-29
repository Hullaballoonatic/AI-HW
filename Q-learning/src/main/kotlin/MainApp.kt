import controller.*
import controller.Settings.OUT_FP
import model.grid.GridWorld
import java.io.*

class MainApp {
    companion object {
        private val out = PrintStream(File(OUT_FP))

        @JvmStatic
        fun main(vararg args: String) =
            QLearner(GridWorld()).build().print(out)
    }
}