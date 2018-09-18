package agents

import java.awt.image.BufferedImage
import java.io.FileNotFoundException
import java.io.InputStream
import java.util.Random
import javax.sound.sampled.AudioInputStream

fun getGFX(filename: String): InputStream {
    val filepath = "gfx/$filename"
    return BufferedImage::class.java.getResourceAsStream(filepath)
        ?: Thread.currentThread().contextClassLoader.getResourceAsStream(filepath)
        ?: throw FileNotFoundException(filepath)
}

fun getSFX(filename: String): InputStream {
    val filepath = "sfx/$filename"
    return AudioInputStream::class.java.getResourceAsStream(filepath)
        ?: Thread.currentThread().contextClassLoader.getResourceAsStream(filepath)
        ?: throw FileNotFoundException(filepath)
}

fun Matrix.map(action: (DoubleArray) -> DoubleArray) {
    for (i in 0 until rows()) action(row(i))
}

fun Matrix.forEach(action: (DoubleArray) -> Unit) {
    for (i in 0 until rows()) action(row(i))
}

val Matrix.rows get() = List<DoubleArray>(rows()) { row(it) }

fun Random.chance(chance: Double) = this.nextInt() < (chance * 100)