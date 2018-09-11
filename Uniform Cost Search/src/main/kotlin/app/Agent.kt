package app

import java.awt.Color.red
import java.awt.Color.yellow
import java.awt.Graphics
import java.awt.event.MouseEvent
import java.lang.System.out
import javax.swing.SwingUtilities.isLeftMouseButton

internal class Agent {
    fun drawPlan(g: Graphics) {
        g.color = red
        try {
            for (step in 0 until path.size - 1) {
                val curStep = path[step]
                val nextStep = path[step + 1]
                g.drawLine(curStep.x, curStep.y, nextStep.x, nextStep.y)
            }
        } catch (ignored: ArrayIndexOutOfBoundsException) {}

        try {
            g.color = yellow
            for (state in pather.frontier) g.drawOval(state.x, state.y, 8, 8)
        } catch (ignored: UninitializedPropertyAccessException) {}
    }

    fun update(m: Model) {
        var e: MouseEvent? = null
        while (true) {
            e = m.controller.nextMouseEvent() ?: break
            goal = e.x to e.y
            curComp = if (isLeftMouseButton(e)) ucs else aStar
            if (curComp == aStar) { GameState.aStar = true
            GameState.goal = Agent.goal!! } else GameState.aStar = false
        }
        if (goal != null) {
            val cur = GameState(m)
            out.println("using " + curComp.toString())
            path =  pather.search(cur, goal!!, curComp)
            try {
                m.setDestination(path[1].x, path[1].y)
            } catch (e: IndexOutOfBoundsException) {
                m.setDestination(goal!!.first.toFloat(), goal!!.second.toFloat())
            }
        }
    }

    companion object {
        lateinit var curComp: Comparator<GameState>
        val aStar = AStarSearchCostComparator()
        val ucs = GameStateCostComparator()
        private var lowestCost: Float = 0f
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