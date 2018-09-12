package app

import java.awt.Color.red
import java.awt.Color.yellow
import java.awt.Graphics
import javax.swing.SwingUtilities.isLeftMouseButton

internal class Agent {
    fun drawPlan(g: Graphics) {
        if (goal != null) {
            path.windowed(2,1) { g.drawLine(red, it[0] to it[1]) }

            frontier.forEach { g.drawCircle(yellow, it, 8) }
        }
    }

    fun update(m: Model) {
        while (true) {
            val e = m.controller.nextMouseEvent() ?: break
            goal = GameState(m, e)
            comp = if (isLeftMouseButton(e)) GameStateCostComparator else AStarSearchCostComparator
        }
        if (goal != null) {
            path =  Pather.search(GameState(m), goal!!, comp)

            try {
                m.setDestination(path[1])
            } catch (_: IndexOutOfBoundsException) {
                m.setDestination(goal)
                goal = null
            }
        }
    }

    companion object {
        lateinit var comp: Comparator<GameState>
        var goal: GameState? = null
            set(value) {
                field = value
                AStarSearchCostComparator.goal = value
            }
        private var path: List<GameState> = emptyList()
        private val frontier get() = Pather.q

        @Throws(Exception::class)
        @JvmStatic
        fun main(args: Array<String>) {
            Controller.playGame()
        }
    }
}