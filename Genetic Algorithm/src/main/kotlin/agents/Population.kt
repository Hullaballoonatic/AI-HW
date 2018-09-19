package agents

import agents.Settings.NUM_SELECTIONS
import java.lang.System.out
import java.util.*
import java.util.concurrent.ThreadLocalRandom

class Population(val size: Int = 100) {
    private val members = List(100) { Member() }.toMutableList()

    private val randomMember
        get() = members[rand.nextInt(members.size)]

    val fittestMember
        get() = members.maxBy { it.fitness }.also { out.println(it) }

    fun selection(times: Int = NUM_SELECTIONS) = repeat(times) {
        val battle = randomMember.battle(randomMember)
        
        members -= if (rand.chance(battle.winner.survivalRate)) battle.loser else battle.winner
    }

    private fun Member.findMate() = List(numCandidateMates) { randomMember }.minBy { candidate -> candidate.similarityTo(this) }!!

    fun population(type: MatingType = MatingType.Crossover, d: Double = 0.5) {
        while (members.size < size) {
            val mother = randomMember

            members += Member().apply {
                for (c in indices) {
                    this[c] = when (type) {
                        MatingType.Cloning -> mother[c]
                        MatingType.Crossover -> {
                            val father = mother.findMate()
                            if (rand.nextBoolean()) mother[c] else father[c]
                        }
                        else -> {
                            val father = mother.findMate()
                            d * mother[c] + (1 - d) * father[c]
                        }
                    }
                }
            }
        }
    }

    fun mutation(catastrophic: Boolean = false) =
        members.forEach {
            if (catastrophic) it.mutate(300, 1.0) else it.mutate()
        }

    companion object {
        val rand: Random get() = ThreadLocalRandom.current()
    }
}

enum class MatingType {
    Crossover, Cloning, Interpolation, Extrapolation
}