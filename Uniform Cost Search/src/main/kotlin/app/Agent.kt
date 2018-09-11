package app

import java.awt.Color.red
import java.awt.Color.yellow
import java.awt.Graphics
import java.awt.event.MouseEvent

internal class Agent {
    fun drawPlan(g: Graphics) {
        g.color = red
        try {
            for (step in 0 until path.size - 1)
                g.drawLine(path[step].x.I, path[step].y.I, path[step + 1].x.I, path[step + 1].y.I)
        } catch (ignored: ArrayIndexOutOfBoundsException) {}

        try {
            g.color = yellow
            for (state in pather.frontier) g.drawOval(state.x.I, state.y.I, 8, 8)
        } catch (ignored: UninitializedPropertyAccessException) {}
    }

    fun update(m: Model) {
        var e: MouseEvent? = null
        while (true) {
            e = m.controller.nextMouseEvent() ?: break
            goal = e.x to e.y
        }
        if (goal != null) {
            val cur = GameState(m)
            val comp = if (e?.button == MouseEvent.BUTTON1) bfs else aStar
            path =  pather.bfs(cur, goal!!, comp)
            try {
                m.setDestination(path[1].x, path[1].y)
            } catch (e: IndexOutOfBoundsException) {
                m.setDestination(goal!!.first.F, goal!!.second.F)
            }
        }
    }

    companion object {
        val aStar = AStarSearchCostComparator()
        val bfs = GameStateCostComparator()
        var lowestCost: Float = 0f
        private val pather = Pather()
        private var goal: Pair<Int,Int>? = null
        private var path: List<GameState> = emptyList()

        @Throws(Exception::class)
        @JvmStatic
        fun main(args: Array<String>) {
            Controller.playGame()
        }

        @JvmStatic
        fun setLowest(lowest: Float) {
            lowestCost = lowest
        }
    }
}