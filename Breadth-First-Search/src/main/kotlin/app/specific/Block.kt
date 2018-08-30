package app.specific

import app.extensions.b
import app.extensions.bytePairListOf

interface IBlock {
    val label: String
    val defaultOccupiedCoordinates: List<Pair<Byte,Byte>>
    var coordinates: Pair<Byte, Byte>
    val currentOccupiedSpaces get() = defaultOccupiedCoordinates.map { (it.first + coordinates.first).b to (it.second + coordinates.second).b }
}

enum class Block(
    override val label: String,
    override val defaultOccupiedCoordinates: List<Pair<Byte, Byte>>,
    override var coordinates: Pair<Byte, Byte> = (0.b to 0.b)
): IBlock {
    ZERO(
        "0", bytePairListOf(
            (3 to 1), (3 to 2),
            (4 to 1), (4 to 2)
        )
    ),
    ONE(
        "1", bytePairListOf(
            (5 to 1),
            (6 to 1), (6 to 2)
        )
    ),
    TWO(
        "2", bytePairListOf(
            (5 to 2), (5 to 3),
            (6 to 3)
        )
    ),
    THREE(
        "3", bytePairListOf(
            (7 to 3),
            (8 to 3), (8 to 4)
        )
    ),
    FOUR(
        "4", bytePairListOf(
            (7 to 4), (7 to 5),
            (8 to 5)
        )
    ),
    FIVE(
        "5", bytePairListOf(
            (7 to 6), (7 to 7),
            (8 to 6)
        )
    ),
    SIX(
        "6", bytePairListOf(
            (4 to 5),
            (5 to 4), (5 to 5),
            (6 to 5)
        )
    ),
    SEVEN(
        "7", bytePairListOf(
            (4 to 6),
            (5 to 6), (5 to 7),
            (6 to 6)
        )
    ),
    EIGHT(
        "8", bytePairListOf(
            (5 to 8),
            (6 to 7), (6 to 8)
        )
    ),
    NINE(
        "9", bytePairListOf(
            (2 to 6),
            (3 to 5), (3 to 6)
        )
    ),
    TEN(
        "10", bytePairListOf(
            (1 to 5), (1 to 6),
            (2 to 5)
        )
    );

    companion object {
        val BLACK_SQUARES = bytePairListOf(
            (0 to 0), (0 to 1), (0 to 2), (0 to 3), (0 to 4), (0 to 5), (0 to 6), (0 to 7), (0 to 8), (0 to 9),
            (1 to 0), (1 to 1), (1 to 2), (1 to 7), (1 to 8), (1 to 9),
            (2 to 0), (2 to 1), (1 to 8), (1 to 9),
            (3 to 0), (3 to 4), (3 to 9),
            (4 to 0), (4 to 3), (4 to 4), (4 to 9),
            (5 to 0), (5 to 9),
            (6 to 0), (6 to 9),
            (7 to 0), (7 to 1), (7 to 8), (7 to 9),
            (8 to 0), (8 to 1), (8 to 2), (8 to 7), (8 to 8), (8 to 9),
            (9 to 0), (9 to 1), (9 to 2), (9 to 3), (9 to 4), (9 to 5), (9 to 6), (9 to 7), (9 to 8), (9 to 9)
        )
    }
}

val blocks = listOf(
    Block.ONE,
    Block.TWO,
    Block.THREE,
    Block.FOUR,
    Block.FIVE,
    Block.SIX,
    Block.SEVEN,
    Block.EIGHT,
    Block.NINE,
    Block.TEN
)
