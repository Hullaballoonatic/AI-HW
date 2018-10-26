package helpers

// ----------------------------------------------------------------
// The contents of this file are distributed under the CC0 license.
// See http://creativecommons.org/publicdomain/zero/1.0/
// ----------------------------------------------------------------

import java.lang.Math.max
import java.lang.Math.min

// Provides several useful static methods for operating on arrays of doubles
object Vec {
    fun DoubleArray.marshal(): Json {
        return Json.newList().also {
            for (vec in this)
                it.add(vec)
        }
    }

    fun Json.unmarshal() = DoubleArray(size) { double(it) }

    val DoubleArray.vecString get() = StringBuilder().apply {
        if (isNotEmpty()) {
            append(get(0))
            for (i in 1 until size) {
                append(",").append(get(i))
            }
        }
    }.toString()

    fun DoubleArray.setAll(v: Double) {
        for (i in this.indices)
            this[i] = v
    }

    fun DoubleArray.normalize() {
        val mag = squaredMagnitude
        if (mag <= 0.0) {
            setAll(0.0)
            this[0] = 1.0
        } else {
            val s = 1.0 / Math.sqrt(mag)
            for (i in indices)
                this[i] *= s
        }
    }

    private val DoubleArray.squaredMagnitude get() = sumByDouble { it * it }

    fun DoubleArray.copy(to: DoubleArray) {
        if (to.size != size)
            throw IllegalArgumentException("mismatching sizes")
        mapIndexed { i, _ -> to[i] }
    }

    fun DoubleArray.copy() = DoubleArray(size) { this[it] }

    operator fun DoubleArray.plusAssign(src: DoubleArray) {
        if (size != src.size)
            throw IllegalArgumentException("mismatching sizes")
        mapIndexed { i, it -> it + src[i] }
    }

    operator fun DoubleArray.times(scalar: Double) = DoubleArray(size) { this[it] * scalar }

    operator fun DoubleArray.timesAssign(scalar: Double) {
        map { it * scalar }
    }

    operator fun DoubleArray.times(o: DoubleArray): Double {
        if (size != o.size)
            throw IllegalArgumentException("mismatching sizes")
        return sumByDouble { it * o[indexOf(it)] }
    }

    fun DoubleArray.squaredDistance(o: DoubleArray): Double {
        if (size != o.size)
            throw IllegalArgumentException("mismatching sizes")
        return sumByDouble { it - o[indexOf(it)] }
    }

    fun DoubleArray.clip(min: Double, max: Double) {
        if (max < min)
            throw IllegalArgumentException("max must be >= min")
        map { max(min, min(max, it)) }
    }

    fun DoubleArray.concatenate(o: DoubleArray) = DoubleArray(size + o.size).also {
        System.arraycopy(this, 0, it, 0, size)
        System.arraycopy(o, 0, it, size, o.size)
    }
}
