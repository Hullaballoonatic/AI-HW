package app.views

// The contents of this file are dedicated to the public domain.
// (See http://creativecommons.org/publicdomain/zero/1.0/)

import app.controllers.Controller
import app.extensions.b
import app.extensions.i
import app.models.Model
import java.awt.Color
import java.awt.Graphics
import java.awt.Image
import java.awt.event.ActionEvent
import java.awt.event.ActionListener
import java.awt.event.WindowEvent
import java.io.File
import java.util.ArrayList
import javax.imageio.ImageIO
import javax.swing.JFrame
import javax.swing.JPanel
import kotlin.experimental.and

class View @Throws(Exception::class)
constructor(private val controller: Controller, private val model: Model) : JFrame(), ActionListener {
    private val slomo: Int = 0

    init {

        // Make the game window
        this.defaultCloseOperation = JFrame.EXIT_ON_CLOSE
        this.title = "Moving Robot"
        this.setSize(1203, 636)
        val panel = MyPanel()
        panel.addMouseListener(controller)
        this.contentPane.add(panel)
        this.isVisible = true

        val replayPoints = ArrayList<Controller>()
    }

    override fun actionPerformed(evt: ActionEvent) {
        repaint() // indirectly calls MyPanel.paintComponent
    }

    internal inner class MyPanel @Throws(Exception::class)
    constructor() : JPanel() {

        private var imageRobot: Image = ImageIO.read(File("robot_blue.png"))

        private fun drawTerrain(g: Graphics) {
            val terrain = model.terrain
            var posBlue = 0
            var posRed = (60 * 60 - 1) * 4
            for (y in 0..59) {
                for (x in 0..59) {
                    val bb = terrain[posBlue + 1] and 0xff.b
                    val gg = terrain[posBlue + 2] and 0xff.b
                    val rr = terrain[posBlue + 3] and 0xff.b
                    g.color = Color(rr.i, gg.i, bb.i)
                    g.fillRect(10 * x, 10 * y, 10, 10)
                    posBlue += 4
                }
                for (x in 60..119) {
                    val bb = terrain[posRed + 1] and 0xff.b
                    val gg = terrain[posRed + 2] and 0xff.b
                    val rr = terrain[posRed + 3] and 0xff.b
                    g.color = Color(rr.i, gg.i, bb.i)
                    g.fillRect(10 * x, 10 * y, 10, 10)
                    posRed -= 4
                }
            }
        }

        private fun drawSprites(g: Graphics) {
            val sprites = model.sprites
            for (s in sprites) {
                // Draw the robot image
                g.drawImage(imageRobot, s.pos.x.i - 12, s.pos.y.i - 32, null)
            }
        }

        public override fun paintComponent(g: Graphics) {
            // Give the agents a chance to make decisions
            if (!controller.update()) {
                this@View.dispatchEvent(WindowEvent(this@View, WindowEvent.WINDOW_CLOSING)) // Close this window
            }

            // Draw the view
            drawTerrain(g)
            drawSprites(g)
            controller.agent.drawPlan(g, model)
        }
    }
}

const val FLAG_IMAGE_HEIGHT = 25