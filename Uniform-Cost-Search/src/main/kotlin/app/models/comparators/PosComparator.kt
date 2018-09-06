package app.models.comparators

import app.models.IxState
import app.models.Position

class PosComparator: Comparator<IxState<Position>> {
    override fun compare(o1: IxState<Position>, o2: IxState<Position>): Int =
        if (o1.data.x == o2.data.x && o1.data.y == o2.data.y) 0 else 1
}

