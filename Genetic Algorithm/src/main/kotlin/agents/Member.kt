package agents

import agents.Settings.DEF_MUTATION_RATE
import agents.Settings.DEF_MUTATION_STD_DEVIATION
import agents.Settings.DEF_NUM_CANDIDATE_MATES
import agents.Settings.DEF_SURVIVAL_RATE
import agents.Settings.NUM_CHROMOSOMES
import agents.Settings.POPULATION_SIZE
import java.util.Random
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

class Member(val chromosomes: List<Double> = List(NUM_CHROMOSOMES) { 0.03 * rand.nextGaussian() }) : Comparable<Member> {
    constructor(init: (index: Int) -> Double) : this(List(NUM_CHROMOSOMES) { init(it) }) {
        for (i in 291 until 295)
            this[i] = init(i)
    }

    val fitness
        get() = -1 * Controller.doBattleNoGui(ReflexAgent(), NeuralAgent(chromosomes.toDoubleArray()))

    var numCandidateMates = DEF_NUM_CANDIDATE_MATES
        set(v) {
            field = min(1, max(v, POPULATION_SIZE))
        }

    var mutationStdDev = DEF_MUTATION_STD_DEVIATION
        set(v) {
            field = min(1.0, max(v, 0.001))
        }

    var mutationRate = DEF_MUTATION_RATE
        set(v) {
            field = min(1.0, max(v, 0.1))
        }

    var survivalRate = DEF_SURVIVAL_RATE
        set(v) {
            field = min(0.6, max(v, 0.5))
        }

    fun similarityTo(candidate: Member) = abs(chromosomes.sum() - candidate.chromosomes.sum())

    fun battle(opponent: Member): BattleResult {
        return if (doBattleNoGui(this, opponent) > 0) BattleResult(this, opponent) else BattleResult(opponent, this)
    }

    fun mutate(numEvents: Int = 10, rate: Double = mutationRate) {
        repeat(numEvents) {
            if (rand.chance(rate)) this[rand.nextInt(NUM_CHROMOSOMES + 4)] += mutationStdDev * rand.nextGaussian()
        }
    }

    operator fun get(c: Int) =
        when (c) {
            291  -> numCandidateMates.toDouble()
            292  -> mutationStdDev
            293  -> mutationRate
            294  -> survivalRate
            else -> chromosomes[c]
        }

    operator fun set(c: Int, v: Double) =
        when (c) {
            291  -> numCandidateMates   = (v * 10).toInt()
            292  -> mutationStdDev      =  v / 50
            293  -> mutationRate        =  v / 10
            294  -> survivalRate        =  v / 10
            else -> chromosomes[c]      =  v
        }

    override fun compareTo(other: Member) =
        when {
            fitness > other.fitness -> -1
            fitness < other.fitness ->  1
            else                    ->  0
        }

    companion object {
        val rand = Random()
    }
}

