package agents

import agents.Game.score

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

object MetaParameters {
    val MUTATION_ODDS get() = 1 to 2 // decrease with wins
    val KILL_ODDS get() = 1 to 3 // decrease with wins
    val NUM_MATES get() = 5 // increase with wins
    val AVG_DEVIATION get() = 0.1 // decrease with wins
    val NUM_TOURNAMENTS get() = 100 // dunno
    val NUM_SELECTIONS get() = 40 // dunno

    override fun toString() = String.format("MUT: $MUTATION_ODDS, KIL: $KILL_ODDS, #TR: $NUM_TOURNAMENTS\n")
}
