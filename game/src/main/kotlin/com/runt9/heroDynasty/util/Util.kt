package com.runt9.heroDynasty.util

fun <T : Comparable<T>> clamp(value: T, range: Pair<T, T>) = clamp(value, range.first, range.second)
fun <T : Comparable<T>> clamp(value: T, min: T, max: T): T {
    return when {
        value < min -> min
        value > max -> max
        else -> value
    }
}