package agents

import agents.Settings.KILL_CHANCE
import agents.Settings.MUTATION_CHANCE
import agents.Settings.NUM_CANDIDATE_MATES
import agents.Settings.NUM_CHROMOSOMES
import agents.Settings.NUM_META_PARAMETERS
import agents.Settings.NUM_SELECTIONS
import java.lang.Math.abs
import java.util.*
import java.util.concurrent.ThreadLocalRandom
import kotlin.math.max
import kotlin.math.min

class Population(val size: Int = 100) : Matrix(size, NUM_CHROMOSOMES + NUM_META_PARAMETERS) {
    init {
        this.map { member ->
            member.map { 0.03 * rand.nextGaussian() }.toDoubleArray()

        }
    }

    var generation = 0

    private val randomM get() = rand.nextInt(rows())
    private val randomMember get() = this.row(randomM)

    val bestFitness: Long get() = List(rows()) { row(it).fitness }.sortedDescending()[0]

    private val DoubleArray.fitness: Long get() = Controller.doBattleNoGui(ReflexAgent(), NeuralAgent(this))

    fun selection(times: Int = NUM_SELECTIONS) = repeat(times) {
        //System.out.println("tournamentSelection")
        val r = randomM
        val red = NeuralAgent(Arrays.copyOfRange(row(r), 0, NUM_CHROMOSOMES))
        val b = randomM
        val blue = NeuralAgent(Arrays.copyOfRange(row(b), 0, NUM_CHROMOSOMES))

        if (Controller.doBattleNoGui(red, blue) < 0)
            if (rand.chance(KILL_CHANCE)) this.removeRow(r) else this.removeRow(b)
        else
            if (rand.chance(KILL_CHANCE)) this.removeRow(b) else this.removeRow(r)
    }

    private fun DoubleArray.similarityTo(to: DoubleArray): Double = abs(this.sum() - to.sum())

    fun population(type: MatingType = MatingType.Crossover, d: Double = 0.5) {
        while (rows() < size) {
            val mother = randomMember

            newRow().mapIndexed { chromosome, _ ->
                if (type == MatingType.Cloning) mother[chromosome]
                else {
                    val father = List<DoubleArray>(NUM_CANDIDATE_MATES.toInt()) { randomMember }.sortedBy { candidate -> candidate.similarityTo(mother) }[0]
                    when(type) {
                        MatingType.Crossover -> if (rand.nextBoolean()) mother[chromosome] else father[chromosome]
                        else -> d * mother[chromosome] + (1-d) * father[chromosome]
                    }
                }
            }
        }
    }

    fun mutation(catastrophic: Boolean = false, deviation: Double = Settings.AVG_DEVIATION) {
        forEach { member ->
            if (catastrophic) member.map { 0.3 * rand.nextGaussian() }
            else repeat(10) {
                if(rand.chance(MUTATION_CHANCE)) member.mutate(rand.nextInt(cols()), deviation * rand.nextGaussian()) }
        }
    }

    private var DoubleArray.mutationChance: Double
        get() = this[291]
        set(v) { this[292] = min(1.0, max(v / 10, 0.1)) }

    private var DoubleArray.avgStdDev: Double
        get() = this[292]
        set(v) { this[292] = min(1.0, max(v / 100, 0.001)) }

    private var DoubleArray.numberCandidateMates: Int
        get() = this[293].toInt()
        set(v) { this[293] = min(size, max(v * 5, 1)).toDouble() }

    private var DoubleArray.winnerLivesChance: Double
        get() = this[294]
        set(v) { this[292] = min(0.53, max(v / 10, 0.1)) }

    private fun DoubleArray.mutate(chromosome: Int, mutation: Double) {
        when (chromosome) {
            291  -> this.mutationChance += mutation
            292  -> this.avgStdDev += mutation
            293  -> this.numberCandidateMates += mutation.toInt()
            294  -> this.winnerLivesChance += mutation
            else -> this[chromosome] += mutation
        }
    }

    companion object {
        val rand: Random get() = ThreadLocalRandom.current()
    }
}

enum class MatingType {
    Crossover, Cloning, Interpolation, Extrapolation
}