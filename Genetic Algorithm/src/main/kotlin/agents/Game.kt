package agents
import agents.Settings.FITNESS_PER_NUM_GENERATIONS
import agents.Settings.NUM_GENERATIONS

internal object Game {
    private val population = Population(size = 100)
    private val fitnessOverTime = List(NUM_GENERATIONS / FITNESS_PER_NUM_GENERATIONS) { 0.0 }

    private fun evolveWeights(): DoubleArray {
        fitnessOverTime.map {
            population.apply {
                repeat(FITNESS_PER_NUM_GENERATIONS) { i ->
                    System.out.print(if(i%10==0)"!" else ".")
                    selection()
                    population()
                    mutation()
                }
            }.fittestMember
        }
        return population.fittestMember.chromosomes.toDoubleArray()
    }

    @Throws(Exception::class)
    @JvmStatic
    fun main(args: Array<String>) {
        Controller.doBattle(ReflexAgent(), NeuralAgent(evolveWeights()))
    }
}