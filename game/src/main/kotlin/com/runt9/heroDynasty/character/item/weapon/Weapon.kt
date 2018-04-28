package com.runt9.heroDynasty.character.item.weapon

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
    val damageRange by lazy {
        if (perfect) {
            Pair(damageRangeRoll.second - damageClampRoll.first, damageRangeRoll.second)
        } else {
            val damageClamp = damageClampRoll.random()
            val minDamage = damageRangeRoll.random(second = damageRangeRoll.second - damageClamp)
            Pair(minDamage, minDamage + damageClamp)
        }
    }

    val accuracy by lazy { perfectOrRandom(accuracyRoll) }
    val critChance by lazy { perfectOrRandom(critChanceRoll) }
    val critDamage by lazy { perfectOrRandom(critDamageRoll) }
    val offHandPenalty by lazy { perfectOrRandom(offHandPenaltyRoll!!) }
}