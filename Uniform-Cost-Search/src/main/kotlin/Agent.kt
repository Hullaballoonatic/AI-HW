import java.awt.event.MouseEvent
import java.awt.Graphics
import java.awt.Color

internal class Agent {

    fun drawPlan(g: Graphics, m: Model) {
        g.color = Color.red
        g.drawLine(m.x.toInt(), m.y.toInt(), m.destinationX.toInt(), m.destinationY.toInt())
    }

    fun update(m: Model) {
        val c = m.controller
        while (true) {
            val e = c.nextMouseEvent() ?: break
            m.setDestination(e.x.toFloat(), e.y.toFloat())
        }
    }

    companion object {
        @Throws(Exception::class)
        @JvmStatic
        fun main(args: Array<String>) {
            Controller.playGame()
        }
    }
}
