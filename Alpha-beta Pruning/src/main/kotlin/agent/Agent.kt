package agent

import chess.ChessState
import chess.Player
import chess.PlayerColor

class Agent(override val game: ChessState, override val color: PlayerColor, intelligenceLevel: Int) : Player() {
    private val intelligence = Intelligence(game, color, intelligenceLevel)
    override fun takeTurn(): Boolean = game.move(intelligence.decide())
}