package helpers

import java.util.Random

val IntRange.randomInt : Int get() {
    if (start == endInclusive) return first
    val lo = if (start < endInclusive) start else endInclusive
    val hi = if (start > endInclusive) start else endInclusive
    return rand.nextInt((hi - lo) + 1) + lo
}

val rand = Random()