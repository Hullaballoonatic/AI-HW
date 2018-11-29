package model.grid

import controller.Action
import controller.Action.MOVE_DOWN
import controller.Action.MOVE_LEFT
import controller.Action.MOVE_RIGHT
import controller.Action.MOVE_UP
import controller.Settings.GOAL_REWARD
import controller.Settings.HAZARD_REWARD
import controller.Settings.NUM_COLS
import controller.Settings.NUM_HAZARDS
import controller.Settings.NUM_ROWS
import model.grid.SpaceType.*
import model.table.*
import java.io.*
import java.util.*

@Suppress("unused")
class GridWorld(val numCols: Int = NUM_COLS, val numRows: Int = NUM_ROWS, val startPosition: Pos = 0 xy 0, private val goalPosition: Pos = numCols - 1 xy numRows - 1, private val goalReward: Double = GOAL_REWARD, hazardPositions: List<Pos>? = null, private val hazardReward: Double = HAZARD_REWARD, numHazards: Int = NUM_HAZARDS, val endPositions: List<Pos> = listOf(goalPosition)) {
    private val hazards: List<Pos> = hazardPositions ?: List(numHazards) {
        var p: Pos
        do {
            val x = rand.nextInt(numCols)
            val y = rand.nextInt(numRows)
            p = x xy y
        } while (p == startPosition || p == goalPosition)
        p
    }

    val size = NUM_COLS * NUM_ROWS

    private val reservedPositions: List<Pos> = (listOf(startPosition, goalPosition) + hazards)

    private val table = Table(numRows = numRows, numCols = numCols) {
        Space(it, when (it) {
            startPosition -> START
            goalPosition -> GOAL
            in hazards -> HAZARD
            else -> NONE
        })
    }

    val spaces get() = table

    val viableActions = HashMap<Pos, List<Action>>(numRows * numCols).apply {
        for (pos in table.data.flatten().map { it.pos }) {
            val actions = arrayListOf<Action>()
            if (pos.col > 0)
                actions += MOVE_LEFT
            if (pos.col < numCols - 1)
                actions += MOVE_RIGHT
            if (pos.row > 0)
                actions += MOVE_UP
            if (pos.row < numRows - 1)
                actions += MOVE_DOWN
            put(pos, actions)
        }
    }

    fun updateMove(pos: Pos, action: Action): Boolean {
        val altered = table[pos].action != action
        table[pos].action = action
        return altered
    }

    fun reward(pos: Pos) = when(table[pos].type) {
        GOAL -> goalReward
        HAZARD -> hazardReward
        else -> 0.0
    }

    /*
    PRINTING SECTION
     */
    private val header: String = StringBuilder().apply {
        append("  ")
        for (x in 0 until numCols)
            append("${x.toSingleChar()} ")
    }.toString()

    private val separator: String = StringBuilder().apply {
        append(" ")
        for (col in 0 until numCols)
            append("+-")
        append("+")
    }.toString()

    private fun Int.toSingleChar() = when {
        this < 10 -> (this + 48).toChar()
        this < 36 -> (this + 55).toChar()
        this < 62 -> (this + 61).toChar()
        else -> '*'
    }

    private fun rowString(y: Int, c: Int = -1): String {
        if (y < 0 || y >= numRows) throw ArrayIndexOutOfBoundsException()
        val sb = StringBuilder().append(y.toSingleChar()).append("|")
        for (x in (0 until numCols))
            if (x == c) sb.append("☺").append("|")
            else sb.append(table[x, y]).append("|")
        return sb.toString()
    }

    fun printWithBoardSeparators(cur: Pos) {
        println(header)
        for (y in 0 until numRows) {
            println(separator)
            if (cur.y == y)
                println(rowString(y, cur.x))
            else println(rowString(y))
        }
        println(separator)
    }

    fun printDirections(output: PrintStream = System.out) {
        for (y in 0 until numRows) {
            for (x in 0 until numCols)
                output.print(table[x, y].action?.label ?: " ")
            output.println()
        }
    }

    fun print(output: PrintStream = System.out) {
        for (y in 0 until numRows) {
            for (x in 0 until numCols)
                output.print(table[x, y])
            output.println()
        }
    }

    companion object {
        val rand = Random()
    }
}
