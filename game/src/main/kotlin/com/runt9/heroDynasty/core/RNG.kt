package com.runt9.heroDynasty.core

import com.runt9.heroDynasty.character.item.Item
import com.runt9.heroDynasty.util.AppConst
import com.runt9.heroDynasty.util.clamp
import com.runt9.heroDynasty.util.getRandom
import org.reflections.Reflections
import squidpony.squidmath.LightRNG
import squidpony.squidmath.RNG
import java.lang.reflect.Modifier
import kotlin.reflect.KClass

val rng = RNG(LightRNG())

data class RandomChanceData(private val rolledNumber: Double, private val threshold: Double) {
    val success get() = rolledNumber < threshold
    val rollSuccessAmount get() = threshold - rolledNumber
}

fun randomChance(baseChance: Double = 50.0, multiplier: Double = 1.0, clampVal: Pair<Double, Double> = 0.0 to 100.0) =
        RandomChanceData(rng.nextDouble() * 100, clamp(baseChance * multiplier, clampVal))
fun randomChance(threshold: Double) = rng.nextDouble() * 100 < threshold

// TODO: Probably need a factory that produces a random item with an actual constructor so we can affect the rarity roll
fun randomItem() = Reflections("com.runt9.heroDynasty.character.item").getSubTypesOf(Item::class.java).filterNot { Modifier.isAbstract(it.modifiers) }.getRandom().newInstance()!!

fun <T : Enum<*>> randomEnum(enum: KClass<T>): T = rng.getRandomElement(enum.java.enumConstants)

fun Pair<Int, Int>.random(first: Int = this.first, second: Int = this.second): Int = rng.between(first, second + 1)
fun Pair<Double, Double>.random(first: Double = this.first, second: Double = this.second): Double = rng.between(first, second + AppConst.DOUBLE_ADDER)

