package view

import tornadofx.*
import view.ticTacToe.TicTacToeView

class MasterView : View() {
    override val root = vbox {
        prefHeight = 160.0
        usePrefHeight = true

        label("Choose a game to play")
        button("TicTacToe") {
            action {
                replaceWith(TicTacToeView::class)
            }
        }
    }
}

