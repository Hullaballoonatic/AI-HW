package model.table

import model.table.TableMajor.*

@Suppress("MemberVisibilityCanBePrivate", "unused")
class Table<T>(val numRows: Int, val numCols: Int, private val major: TableMajor = ROW) {
    lateinit var data: List<T>

    val cols get() = List(numCols) { x -> List(numRows) { y -> get(x, y) } }
    val rows get() = List(numRows) { y -> List(numCols) { x -> get(x, y) } }

    operator fun get(pos: Pos) = get(pos.x, pos.y)
    operator fun get(x: Int, y: Int) = when(major) {
        ROW    -> data[y * numCols + x]
        COLUMN -> data[x * numRows + y]
    }

    fun forEach(action: (T) -> Unit) {
        for (element in data) action(element)
    }

    fun col(c: Int) = List(numRows) { r -> get(c, r) }
    fun row(r: Int) = List(numCols) { c -> get(c, r) }

    companion object {
        operator fun <T> invoke(numCols: Int, numRows: Int, elementConstructor: (Int) -> T) = Table<T>(
            numCols,
            numRows
        ).apply {
            data = List(numCols * numRows) { elementConstructor(it) }
        }
    }
}