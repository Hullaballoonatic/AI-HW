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
(This way, the meta-parameter values can evolve with the population.)
You will need to cut off these extra values before you return the weights for the neural network.
 */

object Settings {
    const val MUTATION_CHANCE = 0.4
    const val KILL_CHANCE = 0.3 // decrease with wins
    const val NUM_CANDIDATE_MATES = 5.0 // increase with wins
    const val AVG_DEVIATION = 0.1 // decrease with wins
    const val NUM_SELECTIONS = 40 // dunno
    const val NUM_GENERATIONS = 10000
    const val FITNESS_PER_GEN_METRIC = 100
    const val NUM_CHROMOSOMES = 291
    const val NUM_META_PARAMETERS = 4
}
