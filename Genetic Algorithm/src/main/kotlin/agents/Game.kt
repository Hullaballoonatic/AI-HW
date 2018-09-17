package agents

internal object Game {
    private fun evolveWeights(): DoubleArray {
        // Create a random initial population

        val population = Population(size = 100)

        repeat(times = MetaParameters.NUM_TOURNAMENTS) {
            population.select()
            population.repopulate()
            population.mutate()
        }
        // Evolve the population
        // todo: YOUR CODE WILL START HERE.
        //       Please write some code to evolve this population.
        //       (For tournament selection, you will need to call Controller.doBattleNoGui(agent1, agent2).)

        // Return an arbitrary member from the population
        return population.row(0)
    }

    @Throws(Exception::class)
    @JvmStatic
    fun main(args: Array<String>) = Controller.doBattle(ReflexAgent(), NeuralAgent(evolveWeights()))
}





