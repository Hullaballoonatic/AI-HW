package app

import java.awt.Color.red
import java.awt.Color.yellow
import java.awt.Graphics
import java.lang.System.out

internal class Agent {

    fun drawPlan(g: Graphics, m: Model) {
        out.println("redrawing")
        g.color = red
        g.drawLine(m.x.I, m.y.I, m.destinationX.I, m.destinationY.I)

        g.color = yellow
        for (state in pather.frontier) g.drawOval(state.x.I, state.y.I,8,8)
    }

    fun update(m: Model) {
        val c = m.controller
        while (true) {
            val e = c.nextMouseEvent() ?: break
            goal.x = e.x.F
            goal.y = e.x.F
        }
        curStep.x = m.x
        curStep.y = m.y
        nextStep = pather.bfs(curStep, goal)
        m.setDestination(nextStep.x, nextStep.y)
    }

    companion object {
        val curStep: GameState = GameState()
        lateinit var nextStep: GameState
        val goal: GameState = GameState()
        val pather = Pather()
        @Throws(Exception::class)
        @JvmStatic
        fun main(args: Array<String>) {
            Controller.playGame()
        }
    }
}