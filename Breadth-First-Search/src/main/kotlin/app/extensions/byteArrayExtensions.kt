package app.extensions

import app.game.allPieces

val ByteArray.coordinatesOutput: String
    get() {
        val sb = StringBuilder()
        for(i in this.indices step 2) {
            sb.append("(" + this[i] + "," + this[i+1] + ") ")
        }
        return sb.trim().toString()
    }

val ByteArray.isValid: Boolean
    get() {
        val board = Array(10) { _ -> BooleanArray(10) {false} }
        allPieces.forEachIndexed { p, piece ->
            piece.getOccupiedSpaces(this[p * 2], this[p * 2 + 1]).forEach { space ->
                if(board[space.col.toInt()][space.row.toInt()] == isOccupied) return false else board[space.col.toInt()][space.row.toInt()] = isOccupied
            }
        }
        return true
    }

const val isOccupied: Boolean = true