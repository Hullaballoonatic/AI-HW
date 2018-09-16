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

fun Matrix.map(action: (DoubleArray) -> Unit) {
    for (i in 0..this.rows()) action(this.row(i))
}

fun Random.playOdds(odds: Pair<Int, Int>): Boolean = this.nextInt(odds.second) in List(odds.first) { it }