package chess

abstract class Player {
    abstract val game: ChessState
    abstract val color: PlayerColor
    abstract fun takeTurn(): Boolean

    override fun toString() = color.toString()
}