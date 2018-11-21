package controller

import model.grid.GridWorld
import tornadofx.App
import view.MainView

class MainApp : App(MainView::class) {
    companion object {
        @JvmStatic
        fun main(vararg args: String) {
            QLearner(GridWorld()).learn()
            return
        }
    }
}