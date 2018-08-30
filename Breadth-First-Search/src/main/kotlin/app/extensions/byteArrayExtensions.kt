package app.extensions

import app.enums.blackSquaresToOccupiedSpacesMap
import app.enums.blocks

val ByteArray.coordinatesOutput: String
    get() {
        val sb = StringBuilder()
        this.toPairList.forEach {
            sb.append("(" + it.first + "," + it.second + ") ")
        }
        return sb.trim().toString()
    }

val ByteArray.isValid: Boolean
    get() {
        val coordinates = this.toPairList
        val occupiedSpaces = blackSquaresToOccupiedSpacesMap.toMutableMap()
        blocks.forEachIndexed { b, block ->
            val potentialConflicts = occupiedSpaces.filterKeys { it != block }.values
            block.coordinates = coordinates[b]
            block.currentOccupiedSpaces.forEach { space ->
                if (space in potentialConflicts) {
                    return false
                } else {
                    occupiedSpaces += block to space
                }
            }
        }
        return true
    }

val ByteArray.toPairList: List<Pair<Byte, Byte>>
    get() {
        val result = ArrayList<Pair<Byte, Byte>>()
        for (i in this.indices step 2) {
            result += this[i] to this[i + 1]
        }
        return result
    }