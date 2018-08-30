package app.functions

import app.extensions.b

fun bytePairListOf(vararg elements: Pair<Int, Int>) = elements.map { it.first.b to it.second.b }