package agents

/*
This algorithm requires several meta-parameters
    the mutation rate,
    the average deviation of each mutation,
    the number of tournaments to conduct,
    the probability that the winner survives,
    and the number of candidate mates that are evaluated for cross-over.
    Use your intuition to determine some good initial values for these meta-parameters.

Add some additional elements to your chromosomes to encode these meta-parameters, and adjust your code to get them from the chromosome.
(This way, the meta-parameter values can evolve with the repopulate.)
You will need to cut off these extra values before you return the weights for the neural network.
 */

object Settings {
    // Default meta-parameters
    const val DEF_MUTATION_RATE = 0.4
    const val DEF_SURVIVAL_RATE = 0.6
    const val DEF_NUM_CANDIDATE_MATES = 5
    const val DEF_MUTATION_STD_DEVIATION = 0.5

    // Additional constants
    const val NUM_SELECTIONS = 10 // dunno
    const val NUM_GENERATIONS = 90
    const val NUM_GENERATIONS_PER_FITNESS = 3

    // Required constants
    const val POPULATION_SIZE = 100
    const val NUM_CHROMOSOMES = 291
}
