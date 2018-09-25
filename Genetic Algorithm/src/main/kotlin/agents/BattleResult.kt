package agents

import java.util.*

data class BattleResult(val winner: Member, val loser: Member) {
    val winnerLives
        get() = winner.survivalRate.procs

    companion object {
        val rand = Random()
    }
}