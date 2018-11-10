package model

class LeafNode(labels: Matrix) : Node {
    val label = labels.run {
        DoubleArray(numCols) { attr ->
            when (valueCount(attr)) {
                0    -> columnMean(attr)
                else -> mostCommonValue(attr)
            }
        }
    }

    override fun toString(): String {
        return with(StringBuilder()) {
            append("[ ")
            for (value in label) append(value).append(" ")
            append("]")
            toString()
        }
    }
}