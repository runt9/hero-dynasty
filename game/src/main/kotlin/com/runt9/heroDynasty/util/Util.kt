package com.runt9.heroDynasty.util

import org.apache.commons.text.WordUtils
import java.math.BigDecimal

fun <T : Comparable<T>> clamp(value: T, range: Pair<T, T>) = clamp(value, range.first, range.second)
fun <T : Comparable<T>> clamp(value: T, min: T, max: T): T {
    return when {
        value < min -> min
        value > max -> max
        else -> value
    }
}

fun Double.toScale(scale: Int) = BigDecimal(this).setScale(scale, BigDecimal.ROUND_HALF_UP).toDouble()
fun String.humanReadable() = WordUtils.capitalize(this.replace("_", " ").toLowerCase())!!
fun String.toAssetName(): String = this.replace(" ", "_").replace("(.)(\\p{Upper})", "$1_$2").toLowerCase()