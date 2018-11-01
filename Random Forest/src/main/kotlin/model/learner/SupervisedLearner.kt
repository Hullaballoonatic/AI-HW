package model.learner

import model.Matrix

abstract class SupervisedLearner {
    // Return the name of this learner
    abstract val name: String

    // Train this supervised learner
    abstract fun train(features: Matrix, labels: Matrix)

    // Make a prediction
    abstract fun predict(receiving: DoubleArray, predicting: DoubleArray)

    // Measures the misclassifications with the provided test data
    fun countMisclassifications(features: Matrix, labels: Matrix): Int {
        if (features.rows != labels.rows)
            throw IllegalArgumentException("Mismatching number of rows")
        val pred = DoubleArray(labels.cols)
        var mis = 0
        for (i in 0 until features.rows) {
            val feat = features.row(i)
            predict(feat, pred)
            val lab = labels.row(i)
            for (j in lab.indices) {
                if (pred[j] != lab[j])
                    mis++
            }
        }
        return mis
    }
}