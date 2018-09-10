package app

import java.awt.Graphics
import java.awt.image.BufferedImage
import java.io.FileNotFoundException
import java.io.InputStream

val Number.I get() = this.toInt()
val Number.F get() = this.toFloat()

val Collection<*>.isNotEmpty get() = !isEmpty()

fun getBufferedImage(str: String): InputStream {
    return Thread.currentThread().contextClassLoader.getResourceAsStream(str)
        ?: BufferedImage::class.java.getResourceAsStream(str)
        ?: throw FileNotFoundException(str)
}

fun Graphics.drawOval(pos: Position, width: Int, height: Int) = this.drawOval(pos.x.I, pos.y.I, width, height)

fun Graphics.drawLine(from: Position, to: Position) = this.drawLine(from.x.I, from.y.I, to.x.I, to.y.I)