package app.game

import java.awt.Color

enum class Piece(
    val id: Int,
    val color: Color = Color(0, 0, 0),
    vararg initialOccupiedSpaces: Pair<Int, Int>
) {
    ZERO(0,
        Color(255, 0, 0),
            (1 to 3), (2 to 3),
            (1 to 4), (2 to 4)
    ),
    ONE(1,
        Color(0, 255, 0),
            (1 to 5),
            (1 to 6), (2 to 6)
    ),
    TWO(2,
        Color(128, 128, 255),
            (2 to 5), (3 to 5),
                      (3 to 6)
    ),
    THREE(3,
        Color(255, 128, 128),
            (3 to 7),
            (3 to 8), (4 to 8)
    ),
    FOUR(4,
        Color(255, 255, 128),
            (4 to 7), (5 to 7),
                      (5 to 8)
    ),
    FIVE(5,
        Color(128, 128, 0),
            (6 to 7), (7 to 7),
            (6 to 8)
    ),
    SIX(6,
        Color(0, 128, 128),
                      (5 to 4),
            (4 to 5), (5 to 5),
                      (5 to 6)
    ),
    SEVEN(7,
        Color(0, 128, 0),
            (6 to 4),
            (6 to 5), (7 to 5),
            (6 to 6)
    ),
    EIGHT(8,
        Color(0, 255, 255),
                      (8 to 5),
            (7 to 6), (8 to 6)
    ),
    NINE(9,
        Color(0, 0, 255),
                      (6 to 2),
            (5 to 3), (6 to 3)
    ),
    TEN(10,
        Color(255, 128, 0),
            (5 to 1), (6 to 1),
            (5 to 2)
    ),
    BLACK_SQUARES(-9,
        Color(0,0,0),
            (0 to 0), (1 to 0), (2 to 0), (3 to 0), (4 to 0), (5 to 0), (6 to 0), (7 to 0), (8 to 0), (9 to 0),
            (0 to 1), (1 to 1), (2 to 1),                                         (7 to 1), (8 to 1), (9 to 1),
            (0 to 2), (1 to 2),                                                             (8 to 2), (9 to 2),
            (0 to 3),                               (4 to 3),                                         (9 to 3),
            (0 to 4),                     (3 to 4), (4 to 4),                                         (9 to 4),
            (0 to 5),                                                                                 (9 to 5),
            (0 to 6),                                                                                 (9 to 6),
            (0 to 7), (1 to 7),                                                             (8 to 7), (9 to 7),
            (0 to 8), (1 to 8), (2 to 8),                                         (7 to 8), (8 to 8), (9 to 8),
            (0 to 9), (1 to 9), (2 to 9), (3 to 9), (4 to 9), (5 to 9), (6 to 9), (7 to 9), (8 to 9), (9 to 9)
    );

    private val initialOccupiedSpaces: List<BoardSpace> = boardSpaceListOf(*initialOccupiedSpaces)
    open fun getOccupiedSpaces(x: Byte = -9, y: Byte = -9) = if(x > 0 && y > 0) initialOccupiedSpaces.map { BoardSpace(it.col + x to it.row + y) } else initialOccupiedSpaces
}

val BLACK_SQUARES = Piece.BLACK_SQUARES

val allPieces = listOf(
    Piece.ZERO,
    Piece.ONE,
    Piece.ONE,
    Piece.TWO,
    Piece.THREE,
    Piece.FOUR,
    Piece.FIVE,
    Piece.SIX,
    Piece.SEVEN,
    Piece.EIGHT,
    Piece.NINE,
    Piece.TEN
)
