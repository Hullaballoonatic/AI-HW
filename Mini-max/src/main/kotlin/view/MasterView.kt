package view

import tornadofx.*
import view.ticTacToe.TicTacToeView

class MasterView : View() {
    override val root = vbox {
        prefHeight = 100.0
        usePrefHeight = true

        label("Choose a ticTacToe to play")
        button("TicTacToe") {
            action {
                replaceWith(TicTacToeView::class)
            }
        }
    }

    override fun onDock() {

    }
}