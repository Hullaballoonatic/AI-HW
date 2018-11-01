package agent

import chess.State
import player.Player
import player.PlayerColor

class Agent(override val game: State, override val color: PlayerColor, intelligenceLevel: Int) : Player() {
    private val intelligence = Intelligence(game, color, intelligenceLevel)
    override fun takeTurn(): Boolean = game.move(intelligence.bestMove())
}