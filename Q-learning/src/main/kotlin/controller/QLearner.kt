package controller

import controller.Action.*
import controller.Settings.DECAY_RATE
import controller.Settings.INITIAL_CONVERGENCE_ALTERATION_RATE_LIMIT
import controller.Settings.INITIAL_CONVERGENCE_ITERATION_LIMIT
import controller.Settings.INITIAL_EXPLORATION_RATE
import controller.Settings.INITIAL_FUTURE_FOCUS_RATE
import controller.Settings.INITIAL_LEARNING_RATE
import controller.Settings.INITIAL_MISTAKE_RATE
import controller.Settings.MISTAKE_RATE_FLOOR
import model.grid.*
import model.table.*
import java.util.*
import kotlin.math.*

/** QLearner
 * qTable
 * mistakeRate     -- 1.0 always make a mistake, 0.0 never
 *                      affects how plastic the learner is to the environment
 * learningRate    -- 1.0 build quickly, 0.0 never build
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
        HashMap<Action, Double>().apply {
            for (action in data.viableActions[it] ?: throw RuntimeException("Invalid Matrix")) {
                put(action, 0.0)
            }
        }
    }

    private fun chooseToExplore(): Boolean {
        val result = rand.nextDouble() < explorationRate
        if (result) explorations++
        return result
    }

    private val mistakeHasOccurred: Boolean get() {
        val result = rand.nextDouble() < mistakeRate
        if (result) mistakes++
        return result
    }

    private fun Pos.valueOf(action: Action) = qTable[this][action] ?: 0.0
    private val Pos.actions: Set<Action> get() = qTable[this].keys
    private val Pos.bestAction: Action get() = qTable[this].maxBy { it.value }?.key ?: actions.random()

    private var iteration = 0

    private val hasConverged: Boolean
        get() {
            if (iteration++ == convergenceIterationLimit) {
                printData()

                if (realAlterationRate < convergenceAlterationRateLimit)
                    return true

                mistakeRate = max(mistakeRate * DECAY_RATE, MISTAKE_RATE_FLOOR)

                iteration     = 0
                alterations   = 0
                explorations  = 0
                exploitations = 0
                mistakes      = 0
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

    private fun Pos.chooseAction(): Action =
        if (chooseToExplore())
            actions.random()
        else { // chooseToExploit
            if (valueOf(bestAction) == 0.0 || mistakeHasOccurred)
                actions.random()
            else {
                exploitations++
                bestAction
            }
        }

    private fun wander(cur: Pos): Pos {
        val action = cur.chooseAction()

        val next = cur.move(action)

        if (data.updateMove(cur, action))
            alterations++

        /** Core Equation
         * Q(i, a) <- (1 - ɑᵏ) * Q(i, a) + ɑᵏ * [ r(i, a, j) + ɣ * max(bϵAⱼ | Q(j, b)) ]
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
                ((data.reward(next)) + futureFocusRate * (qTable[next].values.max()!!))
        return next
    }

    fun build(): GridWorld {
        var next = wander(data.startPosition)
        while (hasNotConverged)
            next = wander(if (next in data.endPositions) data.startPosition else next)
        return data
    }

    // Stats
    private var explorations = 0
    private val realExplorationRate get() = explorations.toDouble() / convergenceIterationLimit

    private var exploitations = 0
    private val realExploitationRate get() = exploitations.toDouble() / convergenceIterationLimit

    private var mistakes = 0
    private val realMistakeRate get() = mistakes.toDouble() / convergenceIterationLimit

    private var alterations = 0
    private val realAlterationRate get() = alterations.toDouble() / convergenceIterationLimit

    // Printing
    private val header = " % alteration  | alteration cap ||    mistake %   |    exploit %   |    explore %    \n" +
                         "---------------+----------------++----------------+----------------+----------------"
    private val statsString: String
        get() = "%.4f".format(realAlterationRate).padStart(14, ' ') + " | " +
                "%.4f".format(convergenceAlterationRateLimit).padEnd(14, ' ') + " || " +
                "%.4f".format(mistakeRate).padStart(7, ' ').padEnd(14, ' ') + " | " +
                "%.4f".format(realExploitationRate).padStart(7, ' ').padEnd(14, ' ') + " | " +
                "%.4f".format(realExplorationRate).padStart(7, ' ').padEnd(14, ' ')


    private fun printData() {
        println()
        println()
        println()
        println()
        println(header)
        println(statsString)
        println()
        data.print()
        println()
        data.printDirections()
    }

    private fun printActionValues() {
        for (space in data.spaces) {
            print("${space.pos} -> ")
            qTable[space.pos].forEach {
                print("( ${it.key} => " + "%.4f ) ".format(it.value).padStart(10, ' '))
            }
            println()
        }
    }

    companion object {
        val rand = Random()
    }
}