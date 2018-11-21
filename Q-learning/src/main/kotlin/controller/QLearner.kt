package controller

import controller.Action.*
import controller.Settings.INITIAL_CONVERGENCE_ALTERATION_RATE_LIMIT
import controller.Settings.INITIAL_CONVERGENCE_ITERATION_LIMIT
import controller.Settings.INITIAL_EXPLORATION_RATE
import controller.Settings.INITIAL_FUTURE_FOCUS_RATE
import controller.Settings.INITIAL_LEARNING_RATE
import controller.Settings.INITIAL_MISTAKE_RATE
import model.grid.*
import model.table.*
import java.util.*

/** QLearner
 * qTable
 * mistakeRate     -- 1.0 always make a mistake, 0.0 never
 *                      affects how plastic the learner is to the environment
 * learningRate    -- 1.0 learn quickly, 0.0 never learn
 * explorationRate -- 1.0 always explore, 0.0 always exploit
 * futureFocusRate -- 1.0 how interested in future events vs current events
 */
class QLearner(
    private val data: GridWorld,
    private var learningRate: Double = INITIAL_LEARNING_RATE,
    private var explorationRate: Double = INITIAL_EXPLORATION_RATE,
    private var mistakeRate: Double = INITIAL_MISTAKE_RATE,
    private var futureFocusRate: Double = INITIAL_FUTURE_FOCUS_RATE,
    private var convergenceIterationLimit: Int = INITIAL_CONVERGENCE_ITERATION_LIMIT,
    private var convergenceAlterationRateLimit: Double = INITIAL_CONVERGENCE_ALTERATION_RATE_LIMIT
) {
    private val qTable: Table<HashMap<Action, Double>> = Table(data.numRows, data.numCols) {
        val x = it % data.numCols
        val y = (it - x) / data.numCols
        val viableActions = data.viableActions[x p y]
        viableActions!!.zip(List(viableActions.size) { 0.0 }).toMap() as HashMap<Action, Double>
    }

    private fun chooseToExplore() = rand.nextDouble() < explorationRate
    private fun chooseToExploit() = !chooseToExplore()

    private val mistakeHasOccurred get() = rand.nextDouble() < mistakeRate
    private val mistakeHasNotOccurred get() = !mistakeHasOccurred

    private var alterations: Int = 0
    private var iteration: Int = 0
    private val hasConverged: Boolean
        get() {
            if (iteration++ == convergenceIterationLimit) {
                val alterationRate = (alterations.toDouble() / convergenceIterationLimit)
                println("$alterationRate : $convergenceAlterationRateLimit")

                println()
                data.print()
                println()

                if (alterationRate < convergenceAlterationRateLimit)
                    return true

                mistakeRate *= 0.99
                iteration = 0
                alterations = 0
            }

            return false
        }
    private val hasNotConverged get() = !hasConverged

    private fun Pos.move(action: Action) = when {
        action == MOVE_LEFT && x > 0 -> Pos(x - 1, y)
        action == MOVE_UP && y > 0 -> Pos(x, y - 1)
        action == MOVE_RIGHT && x < data.numCols - 1 -> Pos(x + 1, y)
        action == MOVE_DOWN && y < data.numRows - 1 -> Pos(x, y + 1)
        else -> {
            data.printWithBoardSeparators(this)
            throw IndexOutOfBoundsException()
        }
    }

    private fun Pos.chooseAction(): Action = let { pos ->
        qTable[pos].run {
            val action = if (chooseToExploit() && mistakeHasNotOccurred)
                keys.maxBy { a -> get(a)!! } ?: keys.random()
            else keys.random()
            return if (qTable[pos][action]!! == 0.0) keys.random() else action
        }
    }

    private fun wander(cur: Pos): Pos {
        val action = cur.chooseAction()

        val next = cur.move(action)

        if (data.updateMove(cur, action))
            alterations++

        /** Core Equation
         * Q(i, a) <- (1 - ɑᵏ) * Q(i, a) + ɑᵏ * [r(i, a, j)+ ɣ * max(bϵAⱼ | Q(j, b))]
         * qTable          = Q
         * learningRate    = ɑᵏ
         * previousState   = i
         * previousAction  = a
         * curState        = j
         * j.action        = b
         * discountFactor  = ɣ // 1.0: foresighted, 0.0 nearsighted
         * r(i,a,j)        = the reward you obtained by being in state i and doing action a to transition to state j
         */
        qTable[cur][action] = (1 - learningRate) * (qTable[cur][action] ?: 0.0) + learningRate *
                ((data.reward(cur)) + futureFocusRate * (qTable[next].values.max()!!))

        return next
    }

    fun learn() {
        var next = wander(data.start)
        while (hasNotConverged)
            next = wander(if (next == data.goal) data.start else next)
        return
    }

    companion object {
        val rand = Random()
    }
}