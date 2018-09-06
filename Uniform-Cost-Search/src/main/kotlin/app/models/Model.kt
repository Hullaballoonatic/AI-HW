package app.models

// The contents of this file are dedicated to the public domain.
// (See http://creativecommons.org/publicdomain/zero/1.0/)

import app.controllers.Controller
import java.awt.image.DataBufferByte
import java.io.File
import java.util.ArrayList
import javax.imageio.ImageIO

class Model(val controller: Controller) {
    // These methods are used internally. They are not useful to the agents.
    lateinit var terrain: ByteArray
    lateinit var sprites: ArrayList<Sprite>
    val pos get() = sprites[0].pos
    var destination
        get() = sprites[0].destination
        set(v) {
            sprites[0].destination = v
        }

    @Throws(Exception::class)
    fun initGame() {
        val bufferedImage = ImageIO.read(File("terrain.png"))
        if (bufferedImage.width != 60 || bufferedImage.height != 60)
            throw Exception("Expected the terrain image to have dimensions of 60-by-60")
        terrain = (bufferedImage.raster.dataBuffer as DataBufferByte).data
        sprites = arrayListOf(Sprite())
    }

    fun update() {
        // Update the agents
        for (sprite in sprites) sprite.update()
    }
}

const val EPSILON = 0.0001f // A small number
const val X_MAX = 1200.0f - EPSILON // The maximum horizontal screen position. (The minimum is 0.)
const val Y_MAX = 600.0f - EPSILON // The maximum vertical screen position. (The minimum is 0.)
