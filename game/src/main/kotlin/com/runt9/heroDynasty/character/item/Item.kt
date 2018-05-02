package com.runt9.heroDynasty.character.item

import com.runt9.heroDynasty.character.Modifier
import com.runt9.heroDynasty.character.ModifierType
import com.runt9.heroDynasty.core.random
import com.runt9.heroDynasty.core.randomChance
import com.runt9.heroDynasty.core.rng
import org.apache.commons.lang3.StringUtils

abstract class Item {
    val modifiers = mutableListOf<Modifier>()

    lateinit var rarity: Rarity
    lateinit var enchantments: MutableList<Enchantment>
    var perfect: Boolean = false

    protected fun addModifier(type: ModifierType, value: Double) = modifiers.add(Modifier(type, value))

    open fun rollStats(rarityModifier: Double = 0.0, baseRoll: Double = 50.0) {
        val roll = rng.nextDouble() * 100

        rarity = when {
            roll < baseRoll * (Rarity.UNIQUE.dropModifier + rarityModifier) -> Rarity.UNIQUE
            roll < baseRoll * (Rarity.VERY_RARE.dropModifier + rarityModifier) -> Rarity.VERY_RARE
            roll < baseRoll * (Rarity.RARE.dropModifier + rarityModifier) -> Rarity.RARE
            roll < baseRoll * (Rarity.UNCOMMON.dropModifier + rarityModifier) -> Rarity.UNCOMMON // TODO: Only from heroes
            else -> Rarity.COMMON
        }

        // TODO: Enchantments
        enchantments = mutableListOf()
        perfect = randomChance(multiplier = rarity.perfectChance).success
    }

    fun perfectOrRandom(pair: Pair<Double, Double>): Double = if (perfect) pair.second else pair.random()

    open fun getNamePrefixes(): List<String> {
        // TODO: Enchantment names
        val prefixes = mutableListOf<String>()
        if (perfect) prefixes.add("Perfect")
        return prefixes.toList()
    }

    open fun getNameSuffixes(): List<String> {
        // TODO: Enchantment names
        return listOf()
    }

    open fun getName(): String {
        val sb = StringBuilder()

        val namePrefixes = getNamePrefixes()
        if (namePrefixes.isNotEmpty()) {
            sb.append(namePrefixes.joinToString(" ")).append(" ")
        }

        sb.append(StringUtils.splitByCharacterTypeCamelCase(this::class.simpleName).joinToString(" "))

        val nameSuffixes = getNameSuffixes()
        if (nameSuffixes.isNotEmpty()) {
            sb.append(" ").append(nameSuffixes.joinToString(" "))
        }

        return sb.toString()
    }
}