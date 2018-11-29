package model.table

data class Pos(val x: Int, val y: Int) {
    val row get() = y
    val col get() = x

    operator fun compareTo(o: Pos): Int {
        return when {
            x > o.x -> 1
            x < o.x -> -1
            else    -> when {
                y > o.y -> 1
                y < o.y -> -1
                else    -> 0
            }
        }
    }

    override fun toString() = "($x, $y)"
}

infix fun Int.xy(o: Int) = Pos(this, o)
infix fun Int.rc(o: Int) = Pos(o, this)