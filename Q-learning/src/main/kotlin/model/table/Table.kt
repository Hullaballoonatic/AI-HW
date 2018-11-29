package model.table

@Suppress("MemberVisibilityCanBePrivate", "unused")
class Table<T>(val numRows: Int, val numCols: Int) : Iterable<T> {
    override fun iterator() = data.flatten().iterator()

    lateinit var data: List<List<T>>

    operator fun get(pos: Pos) = get(pos.x, pos.y)
    operator fun get(x: Int, y: Int) = data[y][x]

    override fun toString(): String {
        val tableString = StringBuilder()
        var vectorString: StringBuilder
        data.forEach { row ->
            vectorString = StringBuilder()
            row.forEach {
                vectorString.append(it.toString()).append(' ')
            }
            tableString.append(vectorString.toString().trim()).append("\n")
        }
        return tableString.toString().trim()
    }

    companion object {
        operator fun <T> invoke(numRows: Int, numCols: Int, elementConstructor: (Pos) -> T) = Table<T>(
            numCols,
            numRows
        ).apply {
            data = List(numRows) { y -> List(numCols) { x -> elementConstructor(x xy y) } }
        }
    }
}