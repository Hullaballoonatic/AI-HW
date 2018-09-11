package app

import java.awt.Graphics
import java.awt.image.BufferedImage
import java.io.FileNotFoundException
import java.io.InputStream

val Collection<*>.isNotEmpty get() = !isEmpty()

val Number.truncate get() = (this.toDouble() * 0.1).toInt()

fun getResource(filepath: String): InputStream =
    BufferedImage::class.java.getResourceAsStream(filepath)
            ?: Thread.currentThread().contextClassLoader.getResourceAsStream(filepath)
            ?: throw FileNotFoundException(filepath)

fun Graphics.drawLine(x1: Number, y1: Number, x2: Number, y2: Number) = this.drawLine(x1.toInt(), y1.toInt(), x2.toInt(), y2.toInt())

fun Graphics.drawOval(x: Number, y: Number, width: Int, height: Int) = this.drawOval(x.toInt(), y.toInt(), width, height)