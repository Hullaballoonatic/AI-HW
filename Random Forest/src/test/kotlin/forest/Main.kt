package forest

// ----------------------------------------------------------------
// The contents of this file are distributed under the CC0 license.
// See http://creativecommons.org/publicdomain/zero/1.0/
// ----------------------------------------------------------------

import model.Matrix
import model.learner.BaselineLearner
import model.learner.SupervisedLearner

object Main {
    private fun test(learner: SupervisedLearner, challenge: String) {
        // Load the training data
        val fn = "data/$challenge"
        val trainFeatures = Matrix()
        trainFeatures.loadARFF("${fn}_train_feat.arff")
        val trainLabels = Matrix()
        trainLabels.loadARFF("${fn}_train_lab.arff")

        // Train the model
        learner.train(trainFeatures, trainLabels)

        // Load the test data
        val testFeatures = Matrix()
        testFeatures.loadARFF("${fn}_test_feat.arff")
        val testLabels = Matrix()
        testLabels.loadARFF("${fn}_test_lab.arff")

        // Measure and report accuracy
        val misclassifications = learner.countMisclassifications(testFeatures, testLabels)
        println("Misclassifications by ${learner.name} at $challenge = $misclassifications / ${testFeatures.rows}")
    }

    private fun testLearner(learner: SupervisedLearner) {
        test(learner, "hep")
        test(learner, "vow")
        test(learner, "soy")
    }

    @JvmStatic fun main(args: Array<String>) {
        testLearner(BaselineLearner())
        // testLearner(new RandomForest(50));
    }
}
