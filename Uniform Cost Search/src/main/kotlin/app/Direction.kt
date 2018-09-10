package app

import app.Direction.*

enum class Direction(val offset: Position) {
     N(Position(  0,  10)),
    NE(Position( 10,  10)),
     E(Position( 10,   0)),
    SE(Position( 10, -10)),
     S(Position(  0, -10)),
    SW(Position(-10, -10)),
     W(Position(-10,   0)),
    NW(Position(-10,  10));
}

val allDirections = listOf(N, NE, E, SE, S, SW, W, NW)