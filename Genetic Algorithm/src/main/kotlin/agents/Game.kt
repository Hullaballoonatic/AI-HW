package agents
import agents.Settings.FITNESS_PER_GEN_METRIC
import agents.Settings.NUM_GENERATIONS
import java.lang.System.out

internal object Game {

    private fun evolveWeights(): DoubleArray {
        val population = Population(size = 100)
        out.print(population)

        val fitnessOverTime = List(NUM_GENERATIONS / FITNESS_PER_GEN_METRIC) { 0.0 }

        fitnessOverTime.mapIndexed { i, _ ->
            population.apply {
                repeat(FITNESS_PER_GEN_METRIC) { _ ->
                    generation++
                    out.print(population)
                    selection()
                    population()
                    mutation(fitnessOverTime[i - 2] == fitnessOverTime[i - 1])
                }
            }.bestFitness
        }

        return population.row(0)
    }

    @Throws(Exception::class)
    @JvmStatic
    fun main(args: Array<String>) {
        Controller.doBattle(ReflexAgent(), NeuralAgent(evolveWeights()))
    }
}