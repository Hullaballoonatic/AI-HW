package chess

enum class PlayerColor {
    BLACK {
        override fun toString() = "Dark"
    },
    WHITE {
        override fun toString() = "Light"
    };
}