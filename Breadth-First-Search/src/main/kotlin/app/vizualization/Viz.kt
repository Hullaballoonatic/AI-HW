package app.vizualization

import javax.swing.JFrame

class Viz @Throws(Exception::class) constructor(): JFrame() {
    private val view = View(this)
    fun setState(value: ByteArray) {
        view.state = value
    }
    init {
        view.addMouseListener(view)
        title = "Puzzle"
        setSize(482, 505)
        contentPane.add(view)
        defaultCloseOperation = JFrame.EXIT_ON_CLOSE
        isVisible = true
    }
}