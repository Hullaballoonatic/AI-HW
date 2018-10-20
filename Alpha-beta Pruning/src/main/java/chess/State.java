package chess;

import player.PlayerColor;

import java.util.ArrayList;
import java.util.Random;

/// Represents the state of a chess game
public class State {
    private static final Random rand = new Random();

    private static final int None = 0;
    private static final int Pawn = 1;
    private static final int Rook = 2;
    private static final int Knight = 3;
    private static final int Bishop = 4;
    private static final int Queen = 5;
    private static final int King = 6;
    private static final int PieceMask = 7;
    private static final int WhiteMask = 8;
    private static final int AllMask = 15;

    private int[] m_rows;

    State() {
        m_rows = new int[8];
        resetBoard();
    }
    
    public State(State that, Move move) {
        State nextState = new State(that);
        nextState.move(move.xSource, move.ySource, move.xDest, move.yDest);
    }

    private State(State that) {
        m_rows = new int[8];
        System.arraycopy(that.m_rows, 0, this.m_rows, 0, 8);
    }

    private int getPiece(int col, int row) {
        return (m_rows[row] >> (4 * col)) & PieceMask;
    }

    private boolean isWhite(int col, int row) {
        return (m_rows[row] >> 4 * col & WhiteMask) > 0;
    }

    /// Sets the piece at location (col, row). If piece is None, then it doesn't
    /// matter what the value of white is.
    private void setPiece(int col, int row, int piece, boolean white) {
        m_rows[row] &= (~(AllMask << (4 * col)));
        m_rows[row] |= ((piece | (white ? WhiteMask : 0)) << (4 * col));
    }

    /// Sets up the board for a new game
    private void resetBoard() {
        setPiece(0, 0, Rook, true);
        setPiece(1, 0, Knight, true);
        setPiece(2, 0, Bishop, true);
        setPiece(3, 0, Queen, true);
        setPiece(4, 0, King, true);
        setPiece(5, 0, Bishop, true);
        setPiece(6, 0, Knight, true);
        setPiece(7, 0, Rook, true);
        for(int i = 0; i < 8; i++)
            setPiece(i, 1, Pawn, true);
        for(int j = 2; j < 6; j++) {
            for(int i = 0; i < 8; i++)
                setPiece(i, j, None, false);
        }
        for(int i = 0; i < 8; i++)
            setPiece(i, 6, Pawn, false);
        setPiece(0, 7, Rook, false);
        setPiece(1, 7, Knight, false);
        setPiece(2, 7, Bishop, false);
        setPiece(3, 7, Queen, false);
        setPiece(4, 7, King, false);
        setPiece(5, 7, Bishop, false);
        setPiece(6, 7, Knight, false);
        setPiece(7, 7, Rook, false);
    }

    /// Positive means white is favored. Negative means black is favored.
    public int heuristic()
    {
        int score = 0;
        for(int y = 0; y < 8; y++)
        {
            for(int x = 0; x < 8; x++)
            {
                int p = getPiece(x, y);
                int value;
                switch(p)
                {
                    case None: value = 0; break;
                    case Pawn: value = 10; break;
                    case Rook: value = 63; break;
                    case Knight: value = 31; break;
                    case Bishop: value = 36; break;
                    case Queen: value = 88; break;
                    case King: value = 500; break;
                    default: throw new RuntimeException("what?");
                }
                if(isWhite(x, y))
                    score += value;
                else
                    score -= value;
            }
        }
        return score + rand.nextInt(3) - 1;
    }

    /// Returns an iterator that iterates over all possible moves for the specified color
    public MoveIterator iterator(PlayerColor color) {
        return new MoveIterator(this, color == PlayerColor.WHITE);
    }

    /// Returns true iff the parameters represent a valid move
    boolean isValidMove(int xSrc, int ySrc, int xDest, int yDest) {
        ArrayList<Integer> possible_moves = moves(xSrc, ySrc);
        for(int i = 0; i < possible_moves.size(); i += 2) {
            if(possible_moves.get(i) == xDest && possible_moves.get(i + 1) == yDest)
                return true;
        }
        return false;
    }

    /// Print a representation of the board to the specified System.out
    void printBoard()
    {
        System.out.println("  A  B  C  D  E  F  G  H");
        System.out.print(" +");
        for(int i = 0; i < 8; i++)
            System.out.print("--+");
        System.out.println();
        for(int j = 7; j >= 0; j--) {
            System.out.print(Character.toString((char)(49 + j)));
            System.out.print("|");
            for(int i = 0; i < 8; i++) {
                int p = getPiece(i, j);
                if(p != None) {
                    if(isWhite(i, j))
                        System.out.print("w");
                    else
                        System.out.print("b");
                }
                switch(p) {
                    case None: System.out.print("  "); break;
                    case Pawn: System.out.print("p"); break;
                    case Rook: System.out.print("r"); break;
                    case Knight: System.out.print("n"); break;
                    case Bishop: System.out.print("b"); break;
                    case Queen: System.out.print("q"); break;
                    case King: System.out.print("K"); break;
                    default: System.out.print("?"); break;
                }
                System.out.print("|");
            }
            System.out.print(Character.toString((char)(49 + j)));
            System.out.print("\n +");
            for(int i = 0; i < 8; i++)
                System.out.print("--+");
            System.out.println();
        }
        System.out.println("  A  B  C  D  E  F  G  H");
    }

    /// Pass in the coordinates of a square with a piece on it
    /// and it will return the places that piece can move to.
    private ArrayList<Integer> moves(int col, int row) {
        ArrayList<Integer> pOutMoves = new ArrayList<>();
        int p = getPiece(col, row);
        boolean bWhite = isWhite(col, row);
        int i, j;
        switch(p) {
            case Pawn:
                if(bWhite) {
                    if(!checkPawnMove(pOutMoves, col, inc(row), false, true) && row == 1)
                        checkPawnMove(pOutMoves, col, inc(inc(row)), false, true);
                    checkPawnMove(pOutMoves, inc(col), inc(row), true, true);
                    checkPawnMove(pOutMoves, dec(col), inc(row), true, true);
                }
                else {
                    if(!checkPawnMove(pOutMoves, col, dec(row), false, false) && row == 6)
                        checkPawnMove(pOutMoves, col, dec(dec(row)), false, false);
                    checkPawnMove(pOutMoves, inc(col), dec(row), true, false);
                    checkPawnMove(pOutMoves, dec(col), dec(row), true, false);
                }
                break;
            case Bishop:
                for(i = inc(col), j=inc(row); true; i = inc(i), j = inc(j))
                    if(checkMove(pOutMoves, i, j, bWhite))
                        break;
                for(i = dec(col), j=inc(row); true; i = dec(i), j = inc(j))
                    if(checkMove(pOutMoves, i, j, bWhite))
                        break;
                for(i = inc(col), j=dec(row); true; i = inc(i), j = dec(j))
                    if(checkMove(pOutMoves, i, j, bWhite))
                        break;
                for(i = dec(col), j=dec(row); true; i = dec(i), j = dec(j))
                    if(checkMove(pOutMoves, i, j, bWhite))
                        break;
                break;
            case Knight:
                checkMove(pOutMoves, inc(inc(col)), inc(row), bWhite);
                checkMove(pOutMoves, inc(col), inc(inc(row)), bWhite);
                checkMove(pOutMoves, dec(col), inc(inc(row)), bWhite);
                checkMove(pOutMoves, dec(dec(col)), inc(row), bWhite);
                checkMove(pOutMoves, dec(dec(col)), dec(row), bWhite);
                checkMove(pOutMoves, dec(col), dec(dec(row)), bWhite);
                checkMove(pOutMoves, inc(col), dec(dec(row)), bWhite);
                checkMove(pOutMoves, inc(inc(col)), dec(row), bWhite);
                break;
            case Rook:
                for(i = inc(col); true; i = inc(i))
                    if(checkMove(pOutMoves, i, row, bWhite))
                        break;
                for(i = dec(col); true; i = dec(i))
                    if(checkMove(pOutMoves, i, row, bWhite))
                        break;
                for(j = inc(row); true; j = inc(j))
                    if(checkMove(pOutMoves, col, j, bWhite))
                        break;
                for(j = dec(row); true; j = dec(j))
                    if(checkMove(pOutMoves, col, j, bWhite))
                        break;
                break;
            case Queen:
                for(i = inc(col); true; i = inc(i))
                    if(checkMove(pOutMoves, i, row, bWhite))
                        break;
                for(i = dec(col); true; i = dec(i))
                    if(checkMove(pOutMoves, i, row, bWhite))
                        break;
                for(j = inc(row); true; j = inc(j))
                    if(checkMove(pOutMoves, col, j, bWhite))
                        break;
                for(j = dec(row); true; j = dec(j))
                    if(checkMove(pOutMoves, col, j, bWhite))
                        break;
                for(i = inc(col), j=inc(row); true; i = inc(i), j = inc(j))
                    if(checkMove(pOutMoves, i, j, bWhite))
                        break;
                for(i = dec(col), j=inc(row); true; i = dec(i), j = inc(j))
                    if(checkMove(pOutMoves, i, j, bWhite))
                        break;
                for(i = inc(col), j=dec(row); true; i = inc(i), j = dec(j))
                    if(checkMove(pOutMoves, i, j, bWhite))
                        break;
                for(i = dec(col), j=dec(row); true; i = dec(i), j = dec(j))
                    if(checkMove(pOutMoves, i, j, bWhite))
                        break;
                break;
            case King:
                checkMove(pOutMoves, inc(col), row, bWhite);
                checkMove(pOutMoves, inc(col), inc(row), bWhite);
                checkMove(pOutMoves, col, inc(row), bWhite);
                checkMove(pOutMoves, dec(col), inc(row), bWhite);
                checkMove(pOutMoves, dec(col), row, bWhite);
                checkMove(pOutMoves, dec(col), dec(row), bWhite);
                checkMove(pOutMoves, col, dec(row), bWhite);
                checkMove(pOutMoves, inc(col), dec(row), bWhite);
                break;
            default:
                break;
        }
        return pOutMoves;
    }

    public boolean move(Move move) {
        return move(move.xSource, move.ySource, move.xDest, move.yDest);
    }

    /// Moves the piece from (xSrc, ySrc) to (xDest, yDest). If this move
    /// gets a pawn across the board, it becomes a queen. If this move
    /// takes a king, then it will remove all pieces of the same color as
    /// the king that was taken and return true to indicate that the move
    /// ended the game.
    public boolean move(int xSrc, int ySrc, int xDest, int yDest) {
        if(xSrc < 0 || xSrc >= 8 || ySrc < 0 || ySrc >= 8)
            throw new RuntimeException("out of range");
        if(xDest < 0 || xDest >= 8 || yDest < 0 || yDest >= 8)
            throw new RuntimeException("out of range");
        int target = getPiece(xDest, yDest);
        int p = getPiece(xSrc, ySrc);
        if(p == None)
            throw new RuntimeException("There is no piece in the source location");
        if(target != None && isWhite(xSrc, ySrc) == isWhite(xDest, yDest))
            throw new RuntimeException("It is illegal to take your own piece");
        if(p == Pawn && (yDest == 0 || yDest == 7))
            p = Queen; // a pawn that crosses the board becomes a queen
        boolean white = isWhite(xSrc, ySrc);
        setPiece(xDest, yDest, p, white);
        setPiece(xSrc, ySrc, None, true);
        if(target == King) {
            // If you take the opponent's king, remove all of the opponent's pieces. This
            // makes sure that look-ahead strategies don't try to look beyond the end of
            // the game (example: sacrifice a king for a king and some other piece.)
            int x, y;
            for(y = 0; y < 8; y++) {
                for(x = 0; x < 8; x++) {
                    if(getPiece(x, y) != None) {
                        if(isWhite(x, y) != white) {
                            setPiece(x, y, None, true);
                        }
                    }
                }
            }
            return true;
        }
        return false;
    }

    private static int inc(int pos) {
        if(pos < 0 || pos >= 7)
            return -1;
        return pos + 1;
    }

    private static int dec(int pos) {
        if(pos < 1)
            return -1;
        return pos -1;
    }

    private boolean checkMove(ArrayList<Integer> pOutMoves, int col, int row, boolean bWhite) {
        if(col < 0 || row < 0)
            return true;
        int p = getPiece(col, row);
        if(p > 0 && isWhite(col, row) == bWhite)
            return true;
        pOutMoves.add(col);
        pOutMoves.add(row);
        return (p > 0);
    }

    private boolean checkPawnMove(ArrayList<Integer> pOutMoves, int col, int row, boolean bDiagonal, boolean bWhite) {
        if(col < 0 || row < 0)
            return true;
        int p = getPiece(col, row);
        if(bDiagonal) {
            if(p == None || isWhite(col, row) == bWhite)
                return true;
        }
        else {
            if(p > 0)
                return true;
        }
        pOutMoves.add(col);
        pOutMoves.add(row);
        return (p > 0);
    }

    /// Represents a possible  move
    public static class Move {
        int xSource;
        int ySource;
        int xDest;
        int yDest;
    }

    /// Iterates through all the possible moves for the specified color.
    public static class MoveIterator
    {
        int x, y;
        ArrayList<Integer> moves;
        State state;
        boolean white;

        /// Constructs a move iterator
        private MoveIterator(State curState, boolean whiteMoves) {
            x = -1;
            y = 0;
            moves = null;
            state = curState;
            white = whiteMoves;
            advance();
        }

        private void advance() {
            if(moves != null && moves.size() >= 2) {
                moves.remove(moves.size() - 1);
                moves.remove(moves.size() - 1);
            }
            while(y < 8 && (moves == null || moves.size() < 2)) {
                if(++x >= 8) {
                    x = 0;
                    y++;
                }
                if(y < 8) {
                    if(state.getPiece(x, y) != State.None && state.isWhite(x, y) == white)
                        moves = state.moves(x, y);
                    else
                        moves = null;
                }
            }
        }

        /// Returns true iff there is another move to visit
        public boolean hasNext() {
            return (moves != null && moves.size() >= 2);
        }

        /// Returns the next move
        public Move next() {
            Move m = new Move();
            m.xSource = x;
            m.ySource = y;
            m.xDest = moves.get(moves.size() - 2);
            m.yDest = moves.get(moves.size() - 1);
            advance();
            return m;
        }
    }
}


