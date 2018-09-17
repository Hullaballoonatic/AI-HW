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
    for (i in 0 until this.rows()) action(this.row(i))
}

fun Random.playOdds(odds: Pair<Int, Int>): Boolean {
    val a = if (odds.first <= 0) 1 else odds.first
    val b = if (odds.second <= 0) 1 else odds.second
    return this.nextInt(b) in List(a) { it }
}

operator fun Pair<Int,Int>.plus(o: Pair<Int,Int>): Pair<Int,Int> = this.first + o.first to this.second * o.second
operator fun Int.times(o: Pair<Int,Int>): Pair<Int,Int> = this * o.first to this * o.second