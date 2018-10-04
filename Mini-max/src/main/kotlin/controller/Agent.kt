package controller

import model.Player
import tornadofx.*

interface Agent<C: Controller> {
    val controller: C
    val player: Player

    fun action()
}

