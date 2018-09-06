package app.visualization

/**
 * Adapted and modified from code provided by Dr. Gashler
 */
/*
internal class View(
    private val viz: Viz,
    private val size: Int = 48,
    initialState: ByteArray = ByteArray(22)
) : JPanel() {
    var state: ByteArray = initialState
        set(value) {
            field = value
            viz.repaint()
        }

    public override fun paintComponent(g: Graphics) {
        BLACK_SQUARES.forEach {
            g.fillRect(size * it.col, size * it.row, size, size)
        }
        for (i in allPieces.indices) {
            g.color = allPieces[i].color
            allPieces[i].getOccupiedSpaces(state[i * 2], state[i * 2 + 1]).forEach {
                g.fillRect(size * it.col, size * it.row, size, size)
            }
        }
    }
}
*/