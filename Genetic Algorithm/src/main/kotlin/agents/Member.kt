package agents

import agents.Settings.DEF_MUTATION_RATE
import agents.Settings.DEF_MUTATION_STD_DEVIATION
import agents.Settings.DEF_NUM_CANDIDATE_MATES
import agents.Settings.DEF_SURVIVAL_RATE
import agents.Settings.NUM_CHROMOSOMES
import agents.Settings.POPULATION_SIZE
import java.lang.System.out
import java.util.Random
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

// SVG

class Member(val chromosomes: MutableList<Double> = MutableList(NUM_CHROMOSOMES) { 0.03 * rand.nextGaussian() }) {
    constructor(init: (index: Int) -> Double) : this(MutableList(NUM_CHROMOSOMES) { init(it) }) {
        for (i in NUM_CHROMOSOMES until NUM_MUTABLE_ATTRIBUTES)
            this[i] = init(i)
    }

    val fitness: Int
        get() {
            val result = Controller.doBattleNoGui(ReflexAgent(), NeuralAgent(chromosomes.toDoubleArray()))
            return if (result != 0L) (-100000.0 / result).toInt() else 0
        }

    var numCandidateMates = DEF_NUM_CANDIDATE_MATES
        set(v) { field = min(1, max(v, POPULATION_SIZE)) }

    private
    var mutationStdDev = DEF_MUTATION_STD_DEVIATION
        set(v) { field = min(1.0, max(v, 0.1)) }

    private
    var mutationRate = DEF_MUTATION_RATE
        set(v) { field = min(1.0, max(v, 0.1)) }

    var survivalRate = DEF_SURVIVAL_RATE
        set(v) { field = min(0.8, max(v, 0.5)) }

    fun similarityTo(candidate: Member) = -abs(chromosomes.sum() - candidate.chromosomes.sum())

    fun battle(opponent: Member): BattleResult =
            if (doBattleNoGui(this, opponent) > 0) BattleResult(this, opponent) else BattleResult(opponent, this)

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
            292  -> mutationStdDev      =  v / 10
            293  -> mutationRate        =  v / 10
            294  -> survivalRate        =  v / 10
            else -> chromosomes[c]      =  v
        }

    companion object {
        val rand = Random()
        const val NUM_MUTABLE_ATTRIBUTES = NUM_CHROMOSOMES + 4
    }

    override fun toString(): String =
            (0 until NUM_MUTABLE_ATTRIBUTES).joinToString(" ", prefix = "$fitness : ") { property -> "% 1.4f ".format(this[property]) }
}