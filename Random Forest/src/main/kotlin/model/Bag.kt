package model

data class Bag(val features: Matrix, val labels: Matrix) {
    companion object {
        fun bootstrap(features: Matrix, labels: Matrix): Bag {
            val baggedFeatures = Matrix(0, features.numCols)
            val baggedLabels = Matrix(0, labels.numCols)
            repeat(features.numRows) {
                val r = (0 until features.numRows).shuffled().first()
                Vec.copy(baggedFeatures.newRow(), features.row(r))
                Vec.copy(baggedLabels.newRow(), labels.row(r))
            }
            return Bag(baggedFeatures, baggedLabels)
        }
    }
}