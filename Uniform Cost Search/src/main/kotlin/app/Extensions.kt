package app

import java.awt.image.BufferedImage
import java.io.FileNotFoundException
import java.io.InputStream

val Number.I get() = this.toInt()
val Number.F get() = this.toFloat()
val Number.D get() = this.toDouble()
val Number.B get() = this.toByte()

val Collection<*>.isNotEmpty get() = !isEmpty()

fun getResource(filepath: String): InputStream {
    return BufferedImage::class.java.getResourceAsStream(filepath)
            ?: Thread.currentThread().contextClassLoader.getResourceAsStream(filepath)
            ?: throw FileNotFoundException(filepath)
}