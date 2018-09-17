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

object MetaParameters {
    var MUTATION_ODDS = 1 to 10
    var KILL_ODDS = 1 to 3
    var NUM_MATES = 3
    var AVG_DEVIATION = 0.03
    var NUM_TOURNAMENTS = 100
}