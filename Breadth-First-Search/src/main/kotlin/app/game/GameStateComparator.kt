package app.game

/**
 * Adapted and modified from code provided by Dr. Gashler
 */
object GameStateComparator: Comparator<ByteArray> {
    override fun compare(a: ByteArray, b: ByteArray): Int {
        for (i in a.indices) {
            if (a[i] < b[i]) return -1
            if (a[i] > b[i]) return 1
        }
        return 0
    }
}