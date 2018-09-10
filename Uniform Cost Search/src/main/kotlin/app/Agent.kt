package app

import java.awt.Color
import java.awt.Graphics
import java.util.PriorityQueue

internal class Agent {

    fun drawPlan(g: Graphics, m: Model) {
        g.color = Color.red
        g.drawLine(m.pos.x.I, m.pos.x.I, m.destination.x.I, m.destination.y.I)

        g.color = Color.yellow
        frontier.forEach { pos -> g.drawOval(pos.x.I, pos.y.I, 8, 8) }
    }

    fun update(m: Model) {
        val c = m.controller
        while (true) {
            val e = c.nextMouseEvent() ?: break
            m.destination = Position(e.x, e.y)
        }
        m.destination = pather.bfs(m.pos, m.destination)
    }

    companion object {
        private val frontier = PriorityQueue<Position>(PositionCostComparator())
        val pather = Pather(frontier)

        @Throws(Exception::class)
        @JvmStatic
        fun main(args: Array<String>) {
            Controller.playGame()
        }
    }
}