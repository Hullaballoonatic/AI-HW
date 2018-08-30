package app.vizualization

import javax.swing.JFrame

class Viz @Throws(Exception::class)
private constructor() : JFrame() {
    init {
        val view = View(this)
        view.addMouseListener(view)
        this.title = "Puzzle"
        this.setSize(482, 505)
        this.contentPane.add(view)
        this.defaultCloseOperation = JFrame.EXIT_ON_CLOSE
        this.isVisible = true
    }

    companion object {

        @Throws(Exception::class)
        @JvmStatic
        fun main(args: Array<String>) {
            Viz()
        }
    }
}