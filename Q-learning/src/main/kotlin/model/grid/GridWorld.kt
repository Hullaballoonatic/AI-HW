package model.grid

import controller.Action
import controller.Action.MOVE_DOWN
import controller.Action.MOVE_LEFT
import controller.Action.MOVE_RIGHT
import controller.Action.MOVE_UP
import controller.Settings.GOAL_LOCATION
import controller.Settings.NUM_COLS
import controller.Settings.NUM_HAZARDS
import controller.Settings.NUM_ROWS
import controller.Settings.START_LOCATION
import model.table.Pos
import model.table.Table
import model.grid.SpaceType.*
import model.table.p
import java.util.*

@Suppress("unused")
class GridWorld(val numCols: Int = NUM_COLS, val numRows: Int = NUM_ROWS, val goal: Pos = GOAL_LOCATION, val start: Pos = START_LOCATION, numHazards: Int = NUM_HAZARDS) {
    private val hazards: List<Pos> = List(numHazards) {
        var p: Pos
        do {
            val x = rand.nextInt(numCols)
            val y = rand.nextInt(numRows)
            p = x p y
        } while (p == start || p == goal)
        p
    }

    val size = NUM_COLS * NUM_ROWS

    private val reservedPositions: List<Pos> = (listOf(start, goal) + hazards)

    private val table = Table(numRows, numCols) {
        val x = it % numCols
        val y = (it - x) / numCols
        val p = x p y
        val type = when (p) {
            start -> START
            goal -> GOAL
            in hazards -> HAZARD
            else -> NONE
        }
        Space(p, type)
    }

    val viableActions = HashMap<Pos, List<Action>>(numRows * numCols).apply {
        for (pos in table.data.map { it.pos }) {
            val actions = arrayListOf<Action>()
            if (pos.x > 0)
                actions += MOVE_LEFT
            if (pos.y > 0)
                actions += MOVE_UP
            if (pos.x < numCols - 1)
                actions += MOVE_RIGHT
            if (pos.y < numRows - 1)
                actions += MOVE_DOWN
            put(pos, actions)
        }
    }

    fun updateMove(pos: Pos, action: Action): Boolean {
        val altered = table[pos].action != action
        table[pos].action = action
        if (pos !in reservedPositions)
            table[pos].type = when(action) {
                MOVE_UP -> SpaceType.MOVE_UP
                MOVE_LEFT -> SpaceType.MOVE_LEFT
                MOVE_DOWN -> SpaceType.MOVE_DOWN
                MOVE_RIGHT -> SpaceType.MOVE_RIGHT
            }
        return altered
    }

    fun reward(pos: Pos) = table[pos].type.reward

    /*
    PRINTING SECTION
     */
    private val header: String = StringBuilder().apply {
        append(" |")
        for (x in 0 until numCols)
            append("${x.toSingleChar()}|")
    }.toString()

    private val separator: String = StringBuilder().apply {
        for (col in 0..numCols)
            append("-+")
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
            else sb.append(table[x, y].type.label).append("|")
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

    fun print() {
        for (y in 0 until numRows) {
            for (x in 0 until numCols)
                print(table[x, y].type.label)
            println()
        }
    }

    companion object {
        val rand = Random()
    }
}