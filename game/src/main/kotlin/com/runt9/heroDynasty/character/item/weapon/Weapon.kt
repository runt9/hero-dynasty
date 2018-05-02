package com.runt9.heroDynasty.character.item.weapon

import com.runt9.heroDynasty.character.ModifierType
import com.runt9.heroDynasty.character.item.Item
import com.runt9.heroDynasty.core.random

abstract class Weapon : Item() {
    // What the item CAN roll
    protected abstract val damageRangeRoll: Pair<Double, Double>
    protected abstract val damageClampRoll: Pair<Double, Double>
    protected abstract val accuracyRoll: Pair<Double, Double>
    protected abstract val critChanceRoll: Pair<Double, Double>
    protected abstract val critDamageRoll: Pair<Double, Double>
    protected open val offHandPenaltyRoll: Pair<Double, Double>? = null // 2H weapons don't use this, so nullable

    abstract val range: Int
    abstract val hands: Int

    // What the item DID roll
    // Lazy initialization is awesome because it allows us to keep these as immutable properties while still
    // deferring the roll ranges to the child classes since lazy init isn't called until a getter is called.
    lateinit var damageRange: Pair<Double, Double>

    override fun rollStats(rarityModifier: Double, baseRoll: Double) {
        super.rollStats(rarityModifier, baseRoll)
        
        addModifier(ModifierType.ACCURACY, perfectOrRandom(accuracyRoll))
        addModifier(ModifierType.CRIT_CHANCE, perfectOrRandom(critChanceRoll))
        addModifier(ModifierType.CRIT_DAMAGE, perfectOrRandom(critDamageRoll))

        damageRange = if (perfect) {
            damageRangeRoll.second - damageClampRoll.first to damageRangeRoll.second
        } else {
            val damageClamp = damageClampRoll.random()
            val minDamage = damageRangeRoll.random(second = damageRangeRoll.second - damageClamp)
            minDamage to minDamage + damageClamp
        }

        if (offHandPenaltyRoll != null) {
            // TODO: off-hand a better way, not a modifier like this
//            addModifier(ModifierType.OFF_HAND, perfectOrRandom(offHandPenaltyRoll!!))
        }
    }
}