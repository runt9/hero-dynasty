package com.runt9.heroDynasty.core

import com.runt9.heroDynasty.util.AppConst
import com.runt9.heroDynasty.util.clamp
import squidpony.squidmath.LightRNG
import squidpony.squidmath.RNG
import kotlin.reflect.KClass

val rng = RNG(LightRNG())
const val baseChance = 50

data class RandomChanceData(val rolledNumber: Double, val threshold: Double) {
    val success get() = rolledNumber < threshold
    val rollSuccessAmount get() = threshold - rolledNumber
}

fun randomChanceReturningData(multiplier: Double = 1.0, clampVal: Pair<Double, Double> = Pair(0.0, 100.0)) =
        RandomChanceData(rng.nextDouble() * 100, clamp(baseChance * multiplier, clampVal))
fun randomChance(multiplier: Double = 1.0, clampVal: Pair<Double, Double> = Pair(0.0, 100.0)) = randomChanceReturningData(multiplier, clampVal).success
fun randomChance(threshold: Double) = rng.nextDouble() * 100 < threshold

fun <T : Enum<*>> randomEnum(enum: KClass<T>): T = rng.getRandomElement(enum.java.enumConstants)

fun Pair<Int, Int>.random(first: Int = this.first, second: Int = this.second): Int = rng.between(first, second + 1)
fun Pair<Double, Double>.random(first: Double = this.first, second: Double = this.second): Double = rng.between(first, second + AppConst.DOUBLE_ADDER)