package puzzle

class GameStateComparator: Comparator<GameState> {
    override fun compare(a: GameState, b: GameState): Int {
        for(i in 0..11) {
            if (a.state[i] > b.state[i]) {
                return -1
            } else if (b.state[i] < a.state[i]) {
                return 1
            }
        }
        return 0
    }
}

private operator fun Pair<Byte, Byte>.compareTo(that: Pair<Byte, Byte>): Int = when {
    this.first  < that.first    ->  -1
    this.second < that.second   ->  -1
    this.first  > that.first    ->   1
    this.second > that.second   ->   1
    else                        ->   0
}