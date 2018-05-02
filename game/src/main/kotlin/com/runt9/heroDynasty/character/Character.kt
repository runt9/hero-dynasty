package com.runt9.heroDynasty.character

import com.runt9.heroDynasty.character.item.Inventory
import com.runt9.heroDynasty.core.unarmedDamageRange
import com.runt9.heroDynasty.util.clamp

abstract class Character(open val name: String) {
    val hitPoints = Resource()
    val isAlive get() = hitPoints.current > 0
    var level = 1
    val modifiers = mutableListOf<Modifier>()
    val skills = mutableListOf<Skill>()
    val inventory = Inventory()

    val isDualWielding get() = inventory.primaryHand != null && inventory.offHand != null

    fun addModifier(type: ModifierType, value: Double) = modifiers.add(Modifier(type, value))
    fun addModifiers(vararg newModifiers: Modifier) = modifiers.addAll(newModifiers)
    fun addModifiers(newModifiers: List<Modifier>) = modifiers.addAll(newModifiers)
    fun getModifier(vararg types: ModifierType, clampVal: Pair<Double, Double> = 0.0 to 100.0): Double = clamp(modifiers.sum(*types), clampVal)

    fun getWeaponDamageRange(offHand: Boolean = false): Pair<Double, Double> {
        val weapon = if (offHand) inventory.offHand else inventory.primaryHand
        return weapon?.damageRange ?: unarmedDamageRange
    }

    open fun recalculateModifiers() {
        // TODO: All modifiers
        modifiers.clear()
        modifiers.addAll(inventory.getModifiers())
    }

    // Some modifiers have special handling and need to be "applied" whenever they change
    // Probably a better way to do this
    open fun applyModifiers(childCheck: ((Modifier) -> Unit)? = null) {
        modifiers.groupByType().forEach { mod ->
            childCheck?.invoke(mod)

            when (mod.type) {
                ModifierType.HP_PER_LEVEL -> hitPoints.perLevel = Resource.BASE_PER_LEVEL * mod.value
                ModifierType.HP_REGEN -> hitPoints.regen = Resource.BASE_REGEN * mod.value
                else -> return@forEach
            }
        }
    }
}