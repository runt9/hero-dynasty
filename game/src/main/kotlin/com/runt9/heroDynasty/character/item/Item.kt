package com.runt9.heroDynasty.character.item

import com.runt9.heroDynasty.core.random
import com.runt9.heroDynasty.core.randomChance

abstract class Item {
    val enchantments = mutableListOf<Enchantment>()

    val rarity: Rarity get() = when(enchantments.sumBy { it.level }) {
        0 -> Rarity.COMMON
        1, 2, 3 -> Rarity.UNCOMMON
        4, 5, 6 -> Rarity.RARE
        7, 8, 9 -> Rarity.VERY_RARE
        else -> Rarity.UNIQUE
    }

    val perfect: Boolean = randomChance(0.01)

    fun perfectOrRandom(pair: Pair<Double, Double>): Double = if (perfect) pair.second else pair.random()
}