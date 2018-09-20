package agents
import agents.Settings.NUM_GENERATIONS
import winningWeights

object Game {
    private
    val population = Population()

    private
    fun evolveWeights() =
            population.apply {
                repeat(NUM_GENERATIONS) {
                    selection()
                    repopulate()
                    mutation()
                }
            }.fittestOverTime.maxBy { it.fitness }!!.chromosomes.toDoubleArray()

    @Throws(Exception::class)
    @JvmStatic
    fun main(args: Array<String>) {
        //Controller.doBattle(ReflexAgent(), NeuralAgent(evolveWeights()))
        //population.printFitnessOverTime()
        Controller.doBattle(ReflexAgent(), NeuralAgent(winningWeights))
    }
}