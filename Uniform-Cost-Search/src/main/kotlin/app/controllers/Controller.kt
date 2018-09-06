package app.controllers

import app.models.Model
import app.models.Position
import app.views.View
import java.awt.event.MouseEvent
import java.awt.event.MouseListener
import java.util.LinkedList
import javax.swing.Timer

class Controller private constructor() : MouseListener {
    var agent: Agent = Agent()
    private var model: Model = Model(this) // holds all the game data
    private lateinit var view: View // the GUI
    private val mouseEvents: LinkedList<MouseEvent> = LinkedList() // a queue of mouse events

    init {
        model.initGame()
    }

    fun update(): Boolean {
        agent.update(model)
        model.update()
        return true
    }

    val nextMouseEvent get() = if (mouseEvents.size == 0) null else mouseEvents.remove()

    override fun mousePressed(e: MouseEvent) {
        if (e.y < 600) {
            mouseEvents.add(e)
            if (mouseEvents.size > 20)
            // discard events if the queue gets big
                mouseEvents.remove()
        }
    }

    override fun mouseReleased(e: MouseEvent) {}
    override fun mouseEntered(e: MouseEvent) {}
    override fun mouseExited(e: MouseEvent) {}
    override fun mouseClicked(e: MouseEvent) {}

    companion object {
        @Throws(Exception::class)
        fun playGame() {
            val c = Controller()
            Position.m = c.model
            c.view = View(
                c,
                c.model
            ) // instantiates a JFrame, which spawns another thread to pump events and keeps the whole program running until the JFrame is closed
            Timer(
                20,
                c.view
            ).start() // creates an ActionEvent at regular intervals, which is handled by View.actionPerformed
        }
    }
}