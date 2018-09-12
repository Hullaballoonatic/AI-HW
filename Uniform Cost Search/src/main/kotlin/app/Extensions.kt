package app

import java.awt.Color
import java.awt.Graphics
import java.awt.event.MouseEvent
import java.awt.image.BufferedImage
import java.io.FileNotFoundException
import java.io.InputStream
import javax.swing.SwingUtilities.isLeftMouseButton

val Collection<*>.isNotEmpty get() = !isEmpty()

val Number.truncate get() = (this.toDouble() * 0.1).toInt()

fun getResource(filepath: String): InputStream =
    BufferedImage::class.java.getResourceAsStream(filepath)
            ?: Thread.currentThread().contextClassLoader.getResourceAsStream(filepath)
            ?: throw FileNotFoundException(filepath)

internal fun Graphics.drawLine(color: Color, points: Pair<GameState, GameState>) {
    this.color = color
    this.drawLine(points.first.x.toInt(), points.first.y.toInt(), points.second.x.toInt(), points.second.y.toInt())
}

internal  fun Graphics.drawCircle(color: Color, a: GameState, circumference: Int) {
    this.color = color
    this.drawOval(a.x.toInt(), a.y.toInt(), circumference, circumference)
}

val MouseEvent.isLeftMouseClick get() = isLeftMouseButton(this)