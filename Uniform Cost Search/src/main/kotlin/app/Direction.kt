package app

import app.Direction.*

enum class Direction(val offX: Float, val offY: Float) {
     N(  0f,  10f),
    NE( 10f,  10f),
     E( 10f,   0f),
    SE( 10f, -10f),
     S(  0f, -10f),
    SW(-10f, -10f),
     W(-10f,   0f),
    NW(-10f,  10f)
}

val allDirections = listOf(N, NE, E, SE, S, SW, W, NW)