package app.vizualization

import app.extensions.b
import java.awt.Color
import java.awt.Graphics
import java.awt.event.MouseEvent
import java.awt.event.MouseListener
import java.util.Random
import javax.swing.JPanel

@Suppress("FunctionName")
internal class View(var viz: Viz) : JPanel(), MouseListener {
    var rand: Random = Random(0)
    var state: ByteArray = ByteArray(22)
    var size: Int = 48
    private lateinit var visual: Graphics

    override fun mousePressed(e: MouseEvent) {
        val index = rand.nextInt(state.size)
        state[index] = (state[index] + if (rand.nextInt(2) == 0) -1 else 1).b

        for (i in 0..10)
            print(
                "(" + state[2 * i] + "," +
                    state[2 * i + 1] + ") "
            )
        println()
        viz.repaint()
    }

    override fun mouseReleased(e: MouseEvent) {}
    override fun mouseEntered(e: MouseEvent) {}
    override fun mouseExited(e: MouseEvent) {}
    override fun mouseClicked(e: MouseEvent) {}

    private fun `draw a block`(x: Int, y: Int) {
        visual.fillRect(size * x, size * y, size, size)
    }

    private fun `draw a 3-block piece`(
        id: Int,
        red: Int, green: Int, blue: Int,
        x1: Int, y1: Int, x2: Int, y2: Int, x3: Int, y3: Int
    ) {
        visual.color = Color(red, green, blue)
        `draw a block`(state[2 * id] + x1, state[2 * id + 1] + y1)
        `draw a block`(state[2 * id] + x2, state[2 * id + 1] + y2)
        `draw a block`(state[2 * id] + x3, state[2 * id + 1] + y3)
    }

    private fun `draw a 4-block piece`(
        id: Int,
        red: Int, green: Int, blue: Int,
        x1: Int, y1: Int, x2: Int, y2: Int,
        x3: Int, y3: Int, x4: Int, y4: Int
    ) {
        `draw a 3-block piece`(id, red, green, blue, x1, y1, x2, y2, x3, y3)
        `draw a block`(state[2 * id] + x4, state[2 * id + 1] + y4)
    }

    public override fun paintComponent(g: Graphics) {
        // Draw the black squares
        visual = g
        g.color = Color(0, 0, 0)
        for (i in 0..9) {
            `draw a block`(i, 0)
            `draw a block`(i, 9)
        }
        for (i in 1..8) {
            `draw a block`(0, i)
            `draw a block`(9, i)
        }
        `draw a block`(1, 1)
        `draw a block`(1, 2)
        `draw a block`(2, 1)
        `draw a block`(7, 1)
        `draw a block`(8, 1)
        `draw a block`(8, 2)
        `draw a block`(1, 7)
        `draw a block`(1, 8)
        `draw a block`(2, 8)
        `draw a block`(8, 7)
        `draw a block`(7, 8)
        `draw a block`(8, 8)
        `draw a block`(3, 4)
        `draw a block`(4, 4)
        `draw a block`(4, 3)

        // Draw the pieces
        `draw a 4-block piece`(0, 255, 0, 0, 1, 3, 2, 3, 1, 4, 2, 4)
        `draw a 3-block piece`(1, 0, 255, 0, 1, 5, 1, 6, 2, 6)
        `draw a 3-block piece`(2, 128, 128, 255, 2, 5, 3, 5, 3, 6)
        `draw a 3-block piece`(3, 255, 128, 128, 3, 7, 3, 8, 4, 8)
        `draw a 3-block piece`(4, 255, 255, 128, 4, 7, 5, 7, 5, 8)
        `draw a 3-block piece`(5, 128, 128, 0, 6, 7, 7, 7, 6, 8)
        `draw a 4-block piece`(6, 0, 128, 128, 5, 4, 5, 5, 5, 6, 4, 5)
        `draw a 4-block piece`(7, 0, 128, 0, 6, 4, 6, 5, 6, 6, 7, 5)
        `draw a 3-block piece`(8, 0, 255, 255, 8, 5, 8, 6, 7, 6)
        `draw a 3-block piece`(9, 0, 0, 255, 6, 2, 6, 3, 5, 3)
        `draw a 3-block piece`(10, 255, 128, 0, 5, 1, 6, 1, 5, 2)
    }
}

