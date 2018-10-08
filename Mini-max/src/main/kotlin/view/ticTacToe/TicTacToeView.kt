package view.ticTacToe

import controller.ticTacToe.TicTacToeAgent
import controller.ticTacToe.TicTacToeController
import model.Position
import tornadofx.*

class TicTacToeView : View() {
    private val c = TicTacToeController()

    override val root = borderpane {
        top = ConsoleView(c).root
        bottom = vbox {
            prefHeight = 120.0
            prefWidth = 100.0
            usePrefHeight
            usePrefWidth
            button("player") {
                prefWidth = 80.0
                usePrefWidth
                action {
                    replaceWith(TicTacToeBoard(c))
                }
            }
            button("bot") {
                prefWidth = 80.0
                usePrefWidth
                action {
                    c.agent = TicTacToeAgent(c)
                    replaceWith(TicTacToeBoard(c))
                }
            }
        }
    }
}

class TicTacToeBoard(private val c: TicTacToeController) : View() {
    init {
        c.start()
    }

    override val root = borderpane {
        top = ConsoleView(c).root

        bottom = gridpane {
            c.board.positions.forEach { pos: Position ->
                button(pos.label) {
                    prefHeight = 30.0
                    prefWidth = 30.0
                    usePrefHeight = true
                    usePrefWidth = true

                    gridpaneConstraints {
                        columnRowIndex(pos.col, pos.row)
                        marginLeftRight(1.0)
                        marginTopBottom(1.0)
                    }

                    action {
                        if (!c.playerLock && pos.isActionable) {
                            c.board.placeToken(pos)
                            c.update()
                        }
                    }
                }
            }
        }
    }
}

class ConsoleView(private val c: TicTacToeController) : View() {
    override val root = vbox {
        label(c.statusProperty)
    }
}