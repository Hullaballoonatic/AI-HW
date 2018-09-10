package app

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