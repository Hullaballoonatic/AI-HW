package puzzle

class BlockComparator : Comparator<Block> {
    override fun compare(block1: Block, block2: Block): Int {
        block1.currentOccupiedSpaces.forEach { b1Coordinates ->
            block2.currentOccupiedSpaces.forEach { b2Coordinates ->
                if (b1Coordinates == b2Coordinates) {
                    return -1
                }
            }
        }
        return 0
    }

    fun gameStateValid(blocks: List<Block>): Boolean {
        // avoid comparing 1 to 2 and 2 to 1, etc
        val checked = ArrayList<Pair<Block, Block>>()
        blocks.forEach { curBlock ->
            (blocks - curBlock).forEach {
                if ((it to curBlock) !in checked) {
                    if (compare(curBlock, it) == -1) {
                        return false
                    }
                    checked += (curBlock to it)
                }
            }
        }
        return true
    }
}