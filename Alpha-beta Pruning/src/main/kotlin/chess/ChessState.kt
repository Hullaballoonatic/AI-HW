package chess

import java.io.PrintStream
import java.util.ArrayList
import java.util.Random
import java.lang.System.out

/// Represents the state of a chess game
class ChessState private constructor(private var board: IntArray = IntArray(BOARD_HEIGHT)) {

    private fun colorMulti(col: Int, row: Int) = if (isWhite(col, row)) 1 else -1

    fun isWhite(col: Int, row: Int) = board[row] shr 4 * col and WHITE_MASK > 0

    fun getPiece(col: Int, row: Int) = board[row] shr 4 * col and PIECE_MASK

    /// Sets the piece at location (col, row). If piece is NONE, then it doesn't
    /// matter what the value of white is.
    private fun setPiece(col: Int, row: Int, piece: Int, white: Boolean) {
        board[row] = board[row] and (ALL_MASK shl 4 * col).inv()
        board[row] = board[row] or (piece or if (white) WHITE_MASK else 0 shl 4 * col)
    }

    /// Positive means white is favored. Negative means black is favored.
    fun heuristic(): Int {
        var score = rand.nextInt(3) - 1
        for (r in rows) {
            for (c in cols) {
                score += colorMulti(c, r) * when (getPiece(c, r)) {
                    NONE   -> 0
                    PAWN   -> 10
                    ROOK   -> 63
                    KNIGHT -> 31
                    BISHOP -> 36
                    QUEEN  -> 88
                    KING   -> 500
                    else   -> throw RuntimeException("what?")
                }
            }
        }
        return score
    }

    /// Returns an iterator that iterates over all possible moves for the specified color
    fun iterator(white: Boolean) = ChessMoveIterator(this, white)

    /// Print a representation of the board to the specified stream
    fun printBoard(stream: PrintStream = out) {
        stream.println("  A  B  C  D  E  F  G  H")
        stream.print(" +")
        for (c in cols)
            stream.print("--+")
        stream.println()
        for (r in rowsDesc) {
            stream.print(Character.toString((49 + r).toChar()))
            stream.print("|")
            for (c in colsDesc) {
                val piece = getPiece(c, r)
                if (piece != NONE)
                    if (isWhite(c, r))
                        stream.print("w")
                    else
                        stream.print("b")
                when (piece) {
                    NONE   -> stream.print("  ")
                    PAWN   -> stream.print("p")
                    ROOK   -> stream.print("r")
                    KNIGHT -> stream.print("n")
                    BISHOP -> stream.print("b")
                    QUEEN  -> stream.print("q")
                    KING   -> stream.print("K")
                    else   -> stream.print("?")
                }
                stream.print("|")
            }
            stream.print(Character.toString((49 + r).toChar()))
            stream.print("\n +")
            for (c in cols)
                stream.print("--+")
            stream.println()
        }
        stream.println("  A  B  C  D  E  F  G  H")
    }

    /// Pass in the coordinates of a square with a piece on it
    /// and it will return the places that piece can move to.

    fun moves(col: Int, row: Int): ArrayList<Int> {
        val pOutMoves = ArrayList<Int>()
        val piece = getPiece(col, row)
        val isWhite = isWhite(col, row)
        var c: Int
        var r: Int
        when (piece) {
            PAWN   -> if (isWhite) {
                if (!checkPawnMove(pOutMoves, col, inc(row), false, isWhite) && row == 1)
                    checkPawnMove(pOutMoves, col, inc(inc(row)), false, isWhite)
                checkPawnMove(pOutMoves, inc(col), inc(row), true, isWhite)
                checkPawnMove(pOutMoves, dec(col), inc(row), true, isWhite)
            } else {
                if (!checkPawnMove(pOutMoves, col, dec(row), false, isWhite) && row == 6)
                    checkPawnMove(pOutMoves, col, dec(dec(row)), false, isWhite)
                checkPawnMove(pOutMoves, inc(col), dec(row), true, isWhite)
                checkPawnMove(pOutMoves, dec(col), dec(row), true, isWhite)
            }
            BISHOP -> {
                c = inc(col)
                r = inc(row)
                while (true) {
                    if (checkMove(pOutMoves, c, r, isWhite))
                        break
                    c = inc(c)
                    r = inc(r)
                }
                c = dec(col)
                r = inc(row)
                while (true) {
                    if (checkMove(pOutMoves, c, r, isWhite))
                        break
                    c = dec(c)
                    r = inc(r)
                }
                c = inc(col)
                r = dec(row)
                while (true) {
                    if (checkMove(pOutMoves, c, r, isWhite))
                        break
                    c = inc(c)
                    r = dec(r)
                }
                c = dec(col)
                r = dec(row)
                while (true) {
                    if (checkMove(pOutMoves, c, r, isWhite))
                        break
                    c = dec(c)
                    r = dec(r)
                }
            }
            KNIGHT -> {
                checkMove(pOutMoves, inc(inc(col)),
                          inc(row), isWhite)
                checkMove(pOutMoves, inc(col),
                          inc(inc(row)), isWhite)
                checkMove(pOutMoves, dec(col),
                          inc(inc(row)), isWhite)
                checkMove(pOutMoves, dec(dec(col)),
                          inc(row), isWhite)
                checkMove(pOutMoves, dec(dec(col)),
                          dec(row), isWhite)
                checkMove(pOutMoves, dec(col),
                          dec(dec(row)), isWhite)
                checkMove(pOutMoves, inc(col),
                          dec(dec(row)), isWhite)
                checkMove(pOutMoves, inc(inc(col)),
                          dec(row), isWhite)
            }
            ROOK  -> {
                c = inc(col)
                while (true) {
                    if (checkMove(pOutMoves, c, row, isWhite))
                        break
                    c = inc(c)
                }
                c = dec(col)
                while (true) {
                    if (checkMove(pOutMoves, c, row, isWhite))
                        break
                    c = dec(c)
                }
                r = inc(row)
                while (true) {
                    if (checkMove(pOutMoves, col, r, isWhite))
                        break
                    r = inc(r)
                }
                r = dec(row)
                while (true) {
                    if (checkMove(pOutMoves, col, r, isWhite))
                        break
                    r = dec(r)
                }
            }
            QUEEN -> {
                c = inc(col)
                while (true) {
                    if (checkMove(pOutMoves, c, row, isWhite))
                        break
                    c = inc(c)
                }
                c = dec(col)
                while (true) {
                    if (checkMove(pOutMoves, c, row, isWhite))
                        break
                    c = dec(c)
                }
                r = inc(row)
                while (true) {
                    if (checkMove(pOutMoves, col, r, isWhite))
                        break
                    r = inc(r)
                }
                r = dec(row)
                while (true) {
                    if (checkMove(pOutMoves, col, r, isWhite))
                        break
                    r = dec(r)
                }
                c = inc(col)
                r = inc(row)
                while (true) {
                    if (checkMove(pOutMoves, c, r, isWhite))
                        break
                    c = inc(c)
                    r = inc(r)
                }
                c = dec(col)
                r = inc(row)
                while (true) {
                    if (checkMove(pOutMoves, c, r, isWhite))
                        break
                    c = dec(c)
                    r = inc(r)
                }
                c = inc(col)
                r = dec(row)
                while (true) {
                    if (checkMove(pOutMoves, c, r, isWhite))
                        break
                    c = inc(c)
                    r = dec(r)
                }
                c = dec(col)
                r = dec(row)
                while (true) {
                    if (checkMove(pOutMoves, c, r, isWhite))
                        break
                    c = dec(c)
                    r = dec(r)
                }
            }
            KING  -> {
                checkMove(pOutMoves, inc(col), row, isWhite)
                checkMove(pOutMoves, inc(col), inc(row), isWhite)
                checkMove(pOutMoves, col, inc(row), isWhite)
                checkMove(pOutMoves, dec(col), inc(row), isWhite)
                checkMove(pOutMoves, dec(col), row, isWhite)
                checkMove(pOutMoves, dec(col), dec(row), isWhite)
                checkMove(pOutMoves, col, dec(row), isWhite)
                checkMove(pOutMoves, inc(col), dec(row), isWhite)
            }
            else                              -> {
            }
        }
        return pOutMoves
    }

    fun move(move: ChessMove): Boolean = move(move.xSource, move.ySource, move.xDest, move.yDest)

    /// Moves the piece from (cSrc, rSrc) to (cDest, rDest). If this move
    /// gets a pawn across the board, it becomes a queen. If this move
    /// takes a king, then it will remove all pieces of the same color as
    /// the king that was taken and return true to indicate that the move
    /// ended the game.
    fun move(cSrc: Int, rSrc: Int, cDest: Int, rDest: Int): Boolean {
        if (cSrc !in cols || rSrc !in rows || cDest !in cols || rDest !in rows)
            throw RuntimeException("out of range")
        val target = getPiece(cDest, rDest)
        var piece = getPiece(cSrc, rSrc)
        if (piece == NONE)
            throw RuntimeException("There is no piece in the source location")
        if (target != NONE && isWhite(cSrc, rSrc) == isWhite(cDest, rDest))
            throw RuntimeException("It is illegal to take your own piece")

        if (piece == PAWN && (rDest == 0 || rDest == 7))
            piece = QUEEN // a pawn that crosses the board becomes a queen
        val isWhite = isWhite(cSrc, rSrc)
        setPiece(cDest, rDest, piece, isWhite)
        setPiece(cSrc, rSrc, NONE, true)
        if (target == KING) {
            // If you take the opponent's king, remove all of the opponent's pieces. This
            // makes sure that look-ahead strategies don't try to look beyond the end of
            // the game (example: sacrifice a king for a king and some other piece.)
            for (r in rows)
                for (c in cols)
                    if (getPiece(c, r) != NONE)
                        if (isWhite(c, r) != isWhite)
                            setPiece(c, r, NONE, true)
            return true
        }
        return false
    }

    private fun checkMove(pOutMoves: ArrayList<Int>, col: Int, row: Int, bWhite: Boolean): Boolean {
        if (col < 0 || row < 0)
            return true
        val piece = getPiece(col, row)
        if (piece > 0 && isWhite(col, row) == bWhite)
            return true
        pOutMoves.add(col)
        pOutMoves.add(row)
        return piece > 0
    }

    private fun checkPawnMove(pOutMoves: ArrayList<Int>, col: Int, row: Int, bDiagonal: Boolean, bWhite: Boolean): Boolean {
        val piece = getPiece(col, row)
        if ((bDiagonal && (piece == NONE || isWhite(col, row) == bWhite)) || piece > 0 || col < 0 || row < 0)
            return true
        pOutMoves.add(col)
        pOutMoves.add(row)
        return piece > 0
    }

    /// Represents a possible  move
    data class ChessMove(val xSource: Int = 0, val ySource: Int = 0, val xDest: Int = 0, val yDest: Int = 0)

    /// Iterates through all the possible moves for the specified color.
    class ChessMoveIterator(private var state: ChessState, private var isWhite: Boolean) {
        private var c = -1
        private var r = 0
        private var moves: ArrayList<Int>? = null

        init {
            advance()
        }

        private fun advance() {
            if (moves!!.size >= 2) {
                moves!!.removeAt(moves!!.size - 1)
                moves!!.removeAt(moves!!.size - 1)
            }
            while (r < BOARD_HEIGHT && (moves == null || moves!!.size < 2)) {
                if (++c >= BOARD_WIDTH) {
                    c = 0
                    r++
                }
                if (r < BOARD_HEIGHT)
                    moves = if (state.getPiece(c, r) != NONE && state.isWhite(c, r) == isWhite) state.moves(c, r) else null
            }
        }

        /// Returns true iff there is another move to visit
        operator fun hasNext() = moves != null && moves!!.size >= 2

        /// Returns the next move
        operator fun next() = ChessMove(c, r, moves!![moves!!.size - 2], moves!![moves!!.size - 1]).also { advance() }

        fun nextState() = ChessState(state, next())
    }

    companion object {
        operator fun invoke(copy: ChessState, move: ChessMove) =
            ChessState().apply {
                for (r in rows)
                    board[r] = copy.board[r]
                move(move)
            }

        operator fun invoke() = ChessState().apply {
            setPiece(0, 0, ROOK,   true)
            setPiece(1, 0, KNIGHT, true)
            setPiece(2, 0, BISHOP, true)
            setPiece(3, 0, QUEEN,  true)
            setPiece(4, 0, KING,   true)
            setPiece(5, 0, BISHOP, true)
            setPiece(6, 0, KNIGHT, true)
            setPiece(7, 0, ROOK,   true)

            for (c in cols)
                setPiece(c, 1, PAWN, true)

            for (r in 2..5)
                for (c in cols)
                    setPiece(c, r, NONE, false)

            for (c in cols)
                setPiece(c, 6, PAWN, false)

            setPiece(0, 7, ROOK,   false)
            setPiece(1, 7, KNIGHT, false)
            setPiece(2, 7, BISHOP, false)
            setPiece(3, 7, QUEEN,  false)
            setPiece(4, 7, KING,   false)
            setPiece(5, 7, BISHOP, false)
            setPiece(6, 7, KNIGHT, false)
            setPiece(7, 7, ROOK,   false)
        }

        val rand = Random()

        private const val BOARD_WIDTH = 8
        const val BOARD_HEIGHT = 8

        val rows = 0 until BOARD_HEIGHT
        val cols = 0 until BOARD_WIDTH
        val rowsDesc = BOARD_HEIGHT downTo 0
        val colsDesc = BOARD_WIDTH downTo 0

        const val MAX_PIECE_MOVES = 27


        const val NONE = 0
        const val PAWN = 1
        const val ROOK = 2
        const val KNIGHT = 3
        const val BISHOP = 4
        const val QUEEN = 5
        const val KING = 6

        const val PIECE_MASK = 7
        const val WHITE_MASK = 8
        const val ALL_MASK = 15

        fun inc(pos: Int) = if (pos < 0 || pos >= BOARD_WIDTH) -1 else pos + 1

        fun dec(pos: Int) = if (pos < 1) -1 else pos - 1
    }
}