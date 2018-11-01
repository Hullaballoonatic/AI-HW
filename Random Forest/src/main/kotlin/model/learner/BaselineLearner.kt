package model.learner

import model.Matrix
import helpers.Vec.copy

// ----------------------------------------------------------------
// The contents of this file are distributed under the CC0 license.
// See http://creativecommons.org/publicdomain/zero/1.0/
// ----------------------------------------------------------------

class BaselineLearner : SupervisedLearner() {
    private lateinit var mode: DoubleArray

    override val name: String get() = "Baseline"

    override fun train(features: Matrix, labels: Matrix) {
        mode = DoubleArray(labels.cols) { if (labels.valueCount(it) == 0) labels.columnMean(it) else labels.mostCommonValue(it) }
    }

    override fun predict(receiving: DoubleArray, predicting: DoubleArray) = predicting.copy(mode)
}
