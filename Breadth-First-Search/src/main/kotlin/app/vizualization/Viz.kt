package app.vizualization

import javax.swing.JFrame

object Viz : JFrame() {
    private val view = View(this)
    fun setState(value: ByteArray) {
        view.state = value
    }

    init {
        title = "Puzzle"
        setSize(482, 505)
        contentPane.add(view)
        defaultCloseOperation = JFrame.EXIT_ON_CLOSE
        isVisible = true
    }
}