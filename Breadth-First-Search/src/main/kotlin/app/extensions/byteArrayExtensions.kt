package app.extensions

val ByteArray.coordinatesOutput: String
    get() = this.asSequence().chunked(2) { it[0] to it[1] }
        .map { "(${it.first},${it.second})" }.joinToString()