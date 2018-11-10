package forest

class LeafNode(labels: Matrix) : Node {
    val label = labels.run {
        DoubleArray(cols()) { attr ->
            when (valueCount(attr)) {
                0    -> columnMean(attr)
                else -> mostCommonValue(attr)
            }
        }
    }
}