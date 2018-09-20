package agents

import agents.Settings.NUM_GENERATIONS_PER_FITNESS
import agents.Settings.NUM_SELECTIONS
import java.lang.IndexOutOfBoundsException
import java.lang.System.out
import java.util.Random
import java.util.concurrent.ThreadLocalRandom

class Population(private val size: Int = 100) {
    private
    var generation: Int = 1

    private
    val members = MutableList(size) { Member() }

    private
    val randomMember
        get() = this[rand.nextInt(members.size)]

    private
    val Member.preferredMate get() =
        List(numCandidateMates) { randomMember }
                .maxBy { it.similarityTo(this) }!!

    private
    val fitnessOverTime: ArrayList<Int> = ArrayList()

    val fittestOverTime: ArrayList<Member> = ArrayList()

    private
    val selectionGen
        get() = generation % NUM_GENERATIONS_PER_FITNESS == 0

    private
    val isStagnated: Boolean
        get() {
            if(fitnessOverTime.size < 8) return false
            else for(i in 1..8) if(fitnessOverTime.last() != fitnessOverTime[fitnessOverTime.size - i]) return false
            return true
        }

    private
    fun kill(member: Member) {
        members -= member
    }

    fun selection(times: Int = NUM_SELECTIONS) =
        if (selectionGen) fitnessSelection()
        else repeat(times) {
            randomMember.battle(randomMember).also { result ->
                if (result.winnerLives) kill(result.loser) else kill(result.winner)
            }
        }

    @Suppress("NestedLambdaShadowedImplicitParameter")
    private
    fun fitnessSelection() {
        members.sortByDescending { it.fitness }
        fittestOverTime += members.first().also { out.println(it) }
        fitnessOverTime += members.first().fitness

        repeat(10) { kill(members.last()) }
        repopulate(MatingType.Cloning, mother = members.first())
    }

    fun repopulate(type: MatingType = MatingType.Crossover, d: Double = 0.5, mother: Member = randomMember, father: Member = mother.preferredMate) {
        generation++
        while (members.size < size) {
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

    fun mutation(catastrophic: Boolean = isStagnated) =
        members.forEach {
            if (catastrophic) it.mutate(300, 1.0) else it.mutate()
        }

    operator fun get(i: Int) = members[i]
    operator fun set(i: Int, v: Member) { members[i] = v }

    fun printFitnessOverTime() {
        for(p in fitnessOverTime) out.println(p)
    }

    enum class MatingType {
        Crossover, Cloning, Interpolation, Extrapolation;
    }

    companion object {
        val rand: Random get() = ThreadLocalRandom.current()
    }
}