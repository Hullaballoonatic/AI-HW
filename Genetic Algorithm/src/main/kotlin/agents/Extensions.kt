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

fun Random.chance(chance: Double) = this.nextInt() < (chance * 100)

fun doBattleNoGui(blue: Member, red: Member) = Controller.doBattleNoGui(NeuralAgent(blue.chromosomes.toDoubleArray()), NeuralAgent(red.chromosomes.toDoubleArray()))