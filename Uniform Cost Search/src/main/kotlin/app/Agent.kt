package app

import java.awt.Color.red
import java.awt.Color.yellow
import java.awt.Graphics

internal class Agent {

    fun drawPlan(g: Graphics, m: Model) {
        g.color = red
        g.drawLine(m.pos, m.destination)

        g.color = yellow
        for (pos in pather.frontier) g.drawOval(pos, 8, 8)
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
        val pather = Pather()
        @Throws(Exception::class)
        @JvmStatic
        fun main(args: Array<String>) {
            Controller.playGame()
        }
    }
}