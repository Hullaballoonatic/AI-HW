import controller.*
import model.grid.*

class MainApp {
    companion object {
        @JvmStatic
        fun main(vararg args: String) {
            val gridWorld = GridWorld(
                numRows = 3,
                numCols = 5,
                numHazards = 4,
                goalReward = 5.0,
                hazardReward = -1.0
            )
            QLearner(
                gridWorld
            ).build().printDirections()
        }
    }
}