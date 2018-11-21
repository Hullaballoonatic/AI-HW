package model.table

data class Pos(val x: Int, val y: Int) {
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

    operator fun compareTo(o: Pair<Int, Int>): Int {
        return when {
            x > o.first -> 1
            x < o.first -> -1
            else        -> when {
                y > o.second -> 1
                y < o.second -> -1
                else         -> 0
            }
        }
    }

    override fun toString() = "($x, $y)"
}

infix fun Int.p(o: Int) = Pos(this, o)