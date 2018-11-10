package model

import model.ValueType.CATEGORICAL
import model.ValueType.CONTINUOUS

/*
 ✓ Your DecisionTree class should inherit bootstrap the SupervisedLearner class.
 ✓ The train method should build a decision tree. This is easiest to implement recursively.
 ✓ Interior nodes will split on feature values. Leaf nodes will store a label vector.
 ✓ The predict method should use the tree to make a prediction.
 ✓ Training is done by recursively dividing the data until fewer than k samples remain.
 ✓ Your leaf nodes should store a label vector. Use the approach that baseline uses to combine the k label vectors into a single label.
 ✓ Your tree should be able to handle both continuous and categorical attributes. (Note that they must be handled differently. You can use the Matrix.valueCount method to determine whether an attribute is continuous or categorical. When you divide into two Matrices, be sure to preserve the Matrix meta-data too, so it will know which attributes are continuous or categorical.)
 ✓ Your interior nodes need only make binary divisions, so each interior node should store an attribute index and a value on which it divides. For continuous values, all samples less than the value split one way, and all samples greater-than-or-equal-to the value split the other way. For categorical values, all samples equal to the value split one way, and all samples not equal to the value split the other way.
 ✓ Choose the divisions by picking a random attribute and sample bootstrap the remaining training features.
 ✓ Make sure your tree is robust against bad splits. In other words, if all the data goes to the same side of the split, don't make an interior node for that, just try to find a better split. If you cannot split the data after several attempts, give up and make a leaf node.
 ✓ You may assume there are no missing values in the data. (Lots of real-world data has missing values, but you don't have to handle that in this assignment.)
 */

class DecisionTree(val name: String = "Decision Tree") : SupervisedLearner() {
    override fun predict(sample: DoubleArray, prediction: DoubleArray) = Vec.copy(prediction, predict(sample))

    override fun name(): String = name

    private var root: Node? = null

    private val isEmpty: Boolean get() = root == null

    private fun build(features: Matrix, labels: Matrix): Node {
        if (features.numRows != labels.numRows) throw Exception("Mismatching features and labels")

        repeat(patience) {
            val c = (0 until features.numCols).shuffled().first()
            val pivot = features.row((0 until features.numRows).shuffled().first())[c]
            val type = if (features.valueCount(c) == 0) CONTINUOUS else CATEGORICAL

            val fa = Matrix(0, features.numCols)
            val la = Matrix(0, labels.numCols)
            val fb = Matrix(0, features.numCols)
            val lb = Matrix(0, labels.numCols)

            features.rows.forEachIndexed { r, featuresRow ->
                val labelsRow = labels.row(r)
                val value = featuresRow[c]
                if ((type == CONTINUOUS && value < pivot) || (type == CATEGORICAL && value == pivot)) {
                    Vec.copy(fa.newRow(), featuresRow)
                    Vec.copy(la.newRow(), labelsRow)
                } else {
                    Vec.copy(fb.newRow(), featuresRow)
                    Vec.copy(lb.newRow(), labelsRow)
                }
            }
            if (fa.numRows != 0 && fb.numRows != 0)
                return InteriorNode(build(fa, la), build(fb, lb), c, pivot, type)
        }

        // a leaf node will have no features in one half so we can just use the original matrix
        return LeafNode(labels)
    }

    override fun train(features: Matrix, labels: Matrix) {
        root = build(features, labels)
        //print()
    }

    fun predict(input: DoubleArray): DoubleArray {
        var curNode = if (!isEmpty) root else throw Exception("This tree has not been trained, and thus cannot make predictions")
        while (true) {
            when (curNode) {
                is LeafNode     -> return curNode.label
                is InteriorNode -> curNode = with(curNode) {
                    when (type) {
                        CONTINUOUS  -> if (input[attributeIndex] <  pivot) a else b
                        CATEGORICAL -> if (input[attributeIndex] == pivot) a else b
                    }
                }
            }
        }
    }

    fun print(root: Node? = this.root, space: Int = 0) {
        when (root) {
            null            -> return
            is InteriorNode -> {
                print(root.b, space + COUNT)
                println()
                repeat(space) { print(" ") }
                println(root)
                print(root.a, space + COUNT)
            }
            else            -> {
                println()
                repeat(space) { print(" ") }
                println(root)
            }
        }
    }

    companion object {
        const val patience = 10
        const val COUNT = 30
    }
}