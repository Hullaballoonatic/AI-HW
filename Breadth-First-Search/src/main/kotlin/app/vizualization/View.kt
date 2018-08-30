package app.vizualization

import app.extensions.b
import app.extensions.coordinatesOutput
import app.game.BLACK_SQUARES
import app.game.Piece
import app.game.allPieces
import kotlinx.collections.immutable.toImmutableList
import java.awt.Graphics
import java.awt.event.MouseEvent
import java.awt.event.MouseListener
import java.util.Random
import javax.swing.JPanel

/**
 * Adapted and modified from code provided by Dr. Gashler
 */

internal class View(
    private val viz: Viz,
    private val interactive: Boolean = false,
    private val size: Int = 48,
    initialState: ByteArray = ByteArray(22)
) : JPanel(), MouseListener {
    private lateinit var gfx: Graphics
    var state: ByteArray = initialState
        set(value) {
            System.out.println("updating gui")
            field = value
            viz.repaint()
        }

    override fun mouseReleased(e: MouseEvent) {}
    override fun mouseEntered(e: MouseEvent) {}
    override fun mouseExited(e: MouseEvent) {}
    override fun mouseClicked(e: MouseEvent) {}
    override fun mousePressed(e: MouseEvent) {
        if (interactive) {
            val index = rand.nextInt(state.size)
            val newState = state.toList().toImmutableList().toByteArray()
            newState[index] = (newState[index] + if (rand.nextInt(2) == 0) -1 else 1).b

            println(newState.coordinatesOutput)
            state = newState
        }
    }

    private fun draw(piece: Piece) {
        gfx.color = piece.color
        piece.getOccupiedSpaces(state[piece.id * 2], state[piece.id * 2 + 1]).forEach { space ->
            gfx.fillRect(size * space.col, size * space.row, size, size)
        }
    }

    public override fun paintComponent(g: Graphics) {
        gfx = g
        BLACK_SQUARES.getOccupiedSpaces().forEach {
            gfx.fillRect(size * it.col, size * it.row, size, size)
        }
        allPieces.forEach { draw(it) }
    }

    companion object {
        private val rand: Random = Random(0)
    }
}

