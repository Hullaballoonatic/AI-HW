package app.specific

object GameStateComparator: Comparator<ByteArray> {
    override fun compare(a: ByteArray, b: ByteArray): Int {
        for (i in a.indices) {
            if (a[i] < b[i])
                return -1
            else if (a[i] > b[i])
                return 1
        }
        return 0
    }
}