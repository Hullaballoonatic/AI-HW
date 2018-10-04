import tornadofx.*
import view.MasterView

class Application : App(MasterView::class)

fun main(args: Array<String>) {
    launch<Application>(args)
}