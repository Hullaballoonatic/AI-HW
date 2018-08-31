package app.game

/**
 * Adapted and modified from code provided by Dr. Gashler
 */
object GameStateComparator: Comparator<ByteArray> {
    override fun compare(arrA: ByteArray, arrB: ByteArray): Int {
        for ((a,b) in arrA.zip(arrB)) {
            if (a < b) return -1
            if (a > b) return 1
        }
        return 0
    }
}