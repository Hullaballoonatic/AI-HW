package forest

data class Bag(val features: Matrix, val labels: Matrix) {
    companion object {
        fun bootstrap(features: Matrix, labels: Matrix): Bag {
            val baggedFeatures = Matrix(0, features.cols())
            val baggedLabels = Matrix(0, labels.cols())
            repeat(features.rows()) {
                (0 until features.rows()).shuffled().first().let { r ->
                    Vec.copy(baggedFeatures.newRow(), features.row(r))
                    Vec.copy(baggedLabels.newRow(), labels.row(r))
                }
            }
            return Bag(baggedFeatures, baggedLabels)
        }
    }
}