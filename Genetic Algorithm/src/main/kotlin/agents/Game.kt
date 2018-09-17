package agents
import agents.MetaParameters.NUM_TOURNAMENTS

internal object Game {
    var score = 0

    private fun evolveWeights(): DoubleArray {
        // Create a random initial population

        val population = Population(size = 100)
        while (NUM_TOURNAMENTS >= 0) {
            repeat(times = NUM_TOURNAMENTS) {
                population.select()
                population.repopulate()
                population.mutate()
            }
            score += Controller.doBattleNoGui(ReflexAgent(), NeuralAgent(population.row(0)))
            System.out.printf("\n-- score: $score\n")
        }
        return population.row(0)
    }

    @Throws(Exception::class)
    @JvmStatic
    fun main(args: Array<String>) {
        Controller.doBattle(ReflexAgent(), NeuralAgent(evolveWeights()))
    }
}





