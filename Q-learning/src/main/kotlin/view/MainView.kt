package view

import tornadofx.*

class MainView : View("Q-Learning") {
    override val root = borderpane {
        button("Settings")
        button("Initialize")
    }
}
