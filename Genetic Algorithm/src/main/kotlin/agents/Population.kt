package agents

import agents.Settings.NUM_SELECTIONS
import java.lang.System.out
import java.util.Random
import java.util.concurrent.ThreadLocalRandom

class Population(val size: Int = 100) {
    private
    val members = List(100) { Member() }.toMutableList()

    private
    val randomMember
        get() = members[rand.nextInt(members.size)]

    val fittestMember
        get() = members.maxBy { it.fitness }.also { out.println(it) }

    fun selection(times: Int = NUM_SELECTIONS) =
        repeat(times) {
            randomMember.battle(randomMember).also { battle ->
                members -= if (rand.chance(battle.winner.survivalRate)) battle.loser else battle.winner
            }
        }

    private
    fun Member.findMate() =
        List(numCandidateMates) { randomMember }
            .minBy { it.similarityTo(this) }!!

    fun population(type: MatingType = MatingType.Crossover, d: Double = 0.5) {
        while (members.size < size) {
            val mother = randomMember
            val father = mother.findMate()

            members += Member { c ->
                when (type) {
                    MatingType.Cloning       -> mother[c]
                    MatingType.Crossover     -> if (rand.nextBoolean()) mother[c] else father[c]
                    MatingType.Interpolation -> d * mother[c] + (1 - d) * father[c]
                    MatingType.Extrapolation -> d * mother[c] + (1 - d) * father[c]
                }
            }
        }
    }

    fun mutation(catastrophic: Boolean = false) =
        members.forEach {
            if (catastrophic) it.mutate(300, 1.0) else it.mutate()
        }

    enum class MatingType {
        Crossover, Cloning, Interpolation, Extrapolation;
    }

    companion object {
        val rand: Random get() = ThreadLocalRandom.current()
    }
}