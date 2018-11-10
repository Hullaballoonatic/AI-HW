package model

enum class ValueType {
    CONTINUOUS {
        override fun toString(): String {
            return "CON"
        }
    },
    CATEGORICAL {
        override fun toString(): String {
            return "CAT"
        }
    }
}