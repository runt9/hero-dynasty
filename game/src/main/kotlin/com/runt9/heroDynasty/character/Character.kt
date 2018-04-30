package com.runt9.heroDynasty.character

import com.runt9.heroDynasty.character.item.Inventory
import com.runt9.heroDynasty.core.unarmedDamageRange
import com.runt9.heroDynasty.util.clamp

abstract class Character(open val name: String) {
    val hitPoints = Resource()
    val isAlive get() = hitPoints.current > 0
    var level = 1
    val modifiers = mutableMapOf<ModifierType, MutableList<Double>>()
    val skills = mutableListOf<Skill>()
    val inventory = Inventory()

    val isDualWielding get() = inventory.primaryHand != null && inventory.offHand != null

    fun addModifier(type: ModifierType, value: Double) = modifiers.addModifier(type, value)
    fun addModifiers(vararg newModifiers: Pair<ModifierType, Double>) = newModifiers.forEach {  modifiers.addModifier(it.first, it.second) }

    fun getModifier(vararg types: ModifierType, clampVal: Pair<Double, Double> = Pair(0.0, 100.0)): Double {
        var count = 0
        val sum = types.sumByDouble {
            count++
            if (modifiers.containsKey(it)) {
                val mods = modifiers[it]!!
                mods.sum() - (mods.size - 1)
            } else {
                1.0
            }
        }

        return clamp(sum - (count - 1), clampVal)
    }

    fun getWeaponDamageRange(offHand: Boolean = false): Pair<Double, Double> {
        val weapon = if (offHand) inventory.offHand else inventory.primaryHand
        return weapon?.damageRange ?: unarmedDamageRange
    }

    fun recalculateModifiers() {
        // TODO: All modifiers
        val weapon = inventory.primaryHand ?: return

        modifiers.clear()

        addModifiers(
                ModifierType.ACCURACY to weapon.accuracy,
                ModifierType.CRIT_CHANCE to weapon.critChance,
                ModifierType.CRIT_DAMAGE to weapon.critDamage
        )
    }
}