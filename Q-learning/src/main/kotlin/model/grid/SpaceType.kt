package model.grid

enum class SpaceType(val label: Char) {
    HAZARD('#'),
    START('S'),
    GOAL('G'),
    NONE(' ');
}