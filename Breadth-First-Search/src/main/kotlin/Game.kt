import com.google.common.collect.ArrayTable
import com.google.common.collect.Table

object Game {
    private val colKeys: List<Int> = MutableList(10) { it }
    private val rowKeys: List<Int> = MutableList(10) { it }
    // The Boolean represents a default traversable block
    private val grid: Table<Int, Int, Boolean> = ArrayTable.create(rowKeys, colKeys)

    @JvmStatic
    fun main(args: List<String>) {
        for (col in colKeys) {
            grid.put(0, col, false)
            grid.put(9, col, false)
        }
    }
}