package app.game

/**
 * Adapted and modified from code provided by Dr. Gashler
 */
object GameStateComparator: Comparator<ByteArray> {
    override fun compare(arrA: ByteArray, arrB: ByteArray): Int {
        for (i in arrA.indices) {
            if (arrA[i] < arrB[i]) return -1
            if (arrA[i] > arrB[i]) return 1
        }
        return 0
    }
}