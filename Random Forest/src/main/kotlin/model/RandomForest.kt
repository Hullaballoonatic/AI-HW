package model

/*
 ✓ It should inherit bootstrap the SupervisedLearner class.
 ✓ The train method should instantiate and train n DecisionTree instances.
 ✓ Generate new training data for each DecisionTree by sampling with replacement. (This is called bootstrapping.) Then, train all the DecisionTrees
 ✓ Give each DecisionTree an equal vote in the predictions. This is called aggregating.
 */
class RandomForest(private val numTrees: Int = 50, val name: String = "Random Forest") : SupervisedLearner() {
    override fun name() = name

    override fun predict(sample: DoubleArray, prediction: DoubleArray) = Vec.copy(prediction, predict(sample))

    lateinit var trees: List<DecisionTree>

    override fun train(features: Matrix, labels: Matrix) {
        trees = List(numTrees) {
            DecisionTree("Tree #${it + 1}").also { tree ->
                Bag.bootstrap(features, labels).let { bag ->
                    tree.train(bag.features, bag.labels)
                }
            }
        }
    }

    private fun predict(input: DoubleArray): DoubleArray {
        val predictions = Matrix(0, 1)
        for (tree in trees) {
            Vec.copy(predictions.newRow(), tree.predict(input))
        }
        return predictions.run {
            DoubleArray(numCols) { attr ->
                mostCommonValue(attr)
            }
        }
    }
}