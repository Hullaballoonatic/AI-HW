package agents

import java.lang.Math.abs
import java.util.Random

class Population(val size: Int = 100) : Matrix(size, NUM_CHROMOSOMES) {
    init {
        this.map { chromosome -> chromosome.map { gene -> gene + 0.03 * rand.nextGaussian() } }
    }

    private val randomChromosomeIndex get() = rand.nextInt(size)
    private val randomChromosome get() = this.row(randomChromosomeIndex)

    fun select(times: Int = 1) = repeat(times) {
        val red = randomChromosomeIndex
        val blue = randomChromosomeIndex
        if (Controller.doBattleNoGui(NeuralAgent(this.row(red)), NeuralAgent(this.row(blue))) == -1)
            if (rand.playOdds(MetaParameters.KILL_ODDS)) this.removeRow(red) else this.removeRow(blue)
        else
            if (rand.playOdds(MetaParameters.KILL_ODDS)) this.removeRow(blue) else this.removeRow(red)
    }

    fun repopulate() {
        while (this.rows() < size) {
            mate()
        }
    }

    private fun similarity(a: DoubleArray, b: DoubleArray): Double = abs(a.sum() - b.sum())
    /*
    (3) For replenishing the population, please implement cross-over to produce new chromosomes that replace the dead ones.
    Randomly choose the first parent. Randomly choose a few candidates for the other parent. Pick the one most similar to the first parent.
    For each element in the child gene, randomly pick one of the two parents to supply its value.
     */
    private fun mate() {
        val mother = randomChromosome
        val father = List<DoubleArray>(MetaParameters.NUM_MATES) { randomChromosome }.asSequence()
            .sortedBy { candidate -> similarity(mother, candidate) }
            .first()
        this.newRow().mapIndexed { n, _ -> if (rand.nextInt(2) == 0) mother[n] else father[n] }.toDoubleArray()
    }

    fun DoubleArray.mutate(deviation: Double = MetaParameters.AVG_DEVIATION) {
        this[rand.nextInt(NUM_CHROMOSOMES)] += deviation * rand.nextGaussian()
    }

    fun mutate(deviation: Double = MetaParameters.AVG_DEVIATION) {
        this.map { member -> if (rand.playOdds(MetaParameters.MUTATION_ODDS)) member.mutate(deviation) }
    }

    companion object {
        val rand: Random = Random()
        const val NUM_CHROMOSOMES = 291
    }
}