package app.controllers

import app.controllers.commands.UniformCostSearch
import app.extensions.i
import app.models.Model
import app.models.comparators.CostComparator
import app.models.comparators.PosComparator
import java.awt.Color
import java.awt.Graphics

class Agent {
    fun drawPlan(g: Graphics, m: Model) {
        g.color = Color.red
        g.drawLine(m.pos.x.i, m.pos.y.i, m.destination.x.i, m.destination.y.i)
    }

    fun update(m: Model) {
        val c = m.controller
        while (true) {
            val e = c.nextMouseEvent ?: break
            m.destination.x = e.x.toFloat()
            m.destination.y = e.y.toFloat()
        }
    }

    companion object {
        val ucs = UniformCostSearch(PosComparator(), CostComparator())

        @Throws(Exception::class)
        @JvmStatic
        fun main(args: Array<String>) {
            Controller.playGame()
        }
    }
}
