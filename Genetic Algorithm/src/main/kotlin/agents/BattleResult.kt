package agents

import java.util.*

data class BattleResult(val winner: Member, val loser: Member) {
    val winnerLives
        get() = rand.chance(winner.survivalRate)

    companion object {
        val rand = Random()
    }
}