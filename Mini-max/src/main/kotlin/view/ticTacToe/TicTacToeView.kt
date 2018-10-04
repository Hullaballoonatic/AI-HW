package view.ticTacToe

import controller.ticTacToe.TicTacToeController
import model.Player
import tornadofx.*

class TicTacToeView : View() {
    private val c: TicTacToeController by inject()

    init {
        c.startGame()
    }

    override val root = gridpane {
        button {
            val here = c.board[0]

            text = "1"
            textFill = Player.NONE.color
            prefHeight = 30.0
            prefWidth = 30.0
            usePrefHeight = true
            usePrefWidth = true

            gridpaneConstraints {
                columnRowIndex(here.y, here.x)
                marginTopBottom(1.0)
                marginLeftRight(1.0)
            }

            action {
                if (c.curPlayer.placeToken(here)) {
                    text = here.label
                    textFill = here.color
                    c.nextPlayer()
                }
            }
        }

        button {
            val here = c.board[1]

            text = "2"
            textFill = Player.NONE.color
            prefHeight = 30.0
            prefWidth = 30.0
            usePrefHeight = true
            usePrefWidth = true

            gridpaneConstraints {
                columnRowIndex(here.y, here.x)
                marginTopBottom(1.0)
                marginLeftRight(1.0)
            }

            action {
                if (c.curPlayer.placeToken(here)) {
                    text = here.label
                    textFill = here.color
                    c.nextPlayer()
                }
            }
        }

        button {
            val here = c.board[2]

            text = "3"
            textFill = Player.NONE.color
            prefHeight = 30.0
            prefWidth = 30.0
            usePrefHeight = true
            usePrefWidth = true

            gridpaneConstraints {
                columnRowIndex(here.y, here.x)
                marginTopBottom(1.0)
                marginLeftRight(1.0)
            }

            action {
                if (c.curPlayer.placeToken(here)) {
                    text = here.label
                    textFill = here.color
                    c.nextPlayer()
                }
            }
        }

        button {
            val here = c.board[3]

            text = "4"
            textFill = Player.NONE.color
            prefHeight = 30.0
            prefWidth = 30.0
            usePrefHeight = true
            usePrefWidth = true

            gridpaneConstraints {
                columnRowIndex(here.y, here.x)
                marginTopBottom(1.0)
                marginLeftRight(1.0)
            }

            action {
                if (c.curPlayer.placeToken(here)) {
                    text = here.label
                    textFill = here.color
                    c.nextPlayer()
                }
            }
        }

        button {
            val here = c.board[4]

            text = "5"
            textFill = Player.NONE.color
            prefHeight = 30.0
            prefWidth = 30.0
            usePrefHeight = true
            usePrefWidth = true

            gridpaneConstraints {
                columnRowIndex(here.y, here.x)
                marginTopBottom(1.0)
                marginLeftRight(1.0)
            }

            action {
                if (c.curPlayer.placeToken(here)) {
                    text = here.label
                    textFill = here.color
                    c.nextPlayer()
                }
            }
        }

        button {
            val here = c.board[5]

            text = "6"
            textFill = Player.NONE.color
            prefHeight = 30.0
            prefWidth = 30.0
            usePrefHeight = true
            usePrefWidth = true

            gridpaneConstraints {
                columnRowIndex(here.y, here.x)
                marginTopBottom(1.0)
                marginLeftRight(1.0)
            }

            action {
                if (c.curPlayer.placeToken(here)) {
                    text = here.label
                    textFill = here.color
                    c.nextPlayer()
                }
            }
        }

        button {
            val here = c.board[6]

            text = "7"
            textFill = Player.NONE.color
            prefHeight = 30.0
            prefWidth = 30.0
            usePrefHeight = true
            usePrefWidth = true

            gridpaneConstraints {
                columnRowIndex(here.y, here.x)
                marginTopBottom(1.0)
                marginLeftRight(1.0)
            }

            action {
                if (c.curPlayer.placeToken(here)) {
                    text = here.label
                    textFill = here.color
                    c.nextPlayer()
                }
            }
        }

        button {
            val here = c.board[7]

            text = "8"
            textFill = Player.NONE.color
            prefHeight = 30.0
            prefWidth = 30.0
            usePrefHeight = true
            usePrefWidth = true

            gridpaneConstraints {
                columnRowIndex(here.y, here.x)
                marginTopBottom(1.0)
                marginLeftRight(1.0)
            }

            action {
                if (c.curPlayer.placeToken(here)) {
                    text = here.label
                    textFill = here.color
                    c.nextPlayer()
                }
            }
        }

        button {
            val here = c.board[8]

            text = "9"
            textFill = Player.NONE.color
            prefHeight = 30.0
            prefWidth = 30.0
            usePrefHeight = true
            usePrefWidth = true

            gridpaneConstraints {
                columnRowIndex(here.y, here.x)
                marginTopBottom(1.0)
                marginLeftRight(1.0)
            }

            action {
                if (c.curPlayer.placeToken(here)) {
                    text = here.label
                    textFill = here.color
                    c.nextPlayer()
                }
            }
        }
    }
}