package com.runt9.heroDynasty.character.item.weapon

import com.runt9.heroDynasty.character.ModifierType

class Shield : Weapon() {
    override val damageRangeRoll = 0.4 to 0.65
    override val damageClampRoll = 0.04 to 0.12
    override val accuracyRoll = 1.2 to 1.4
    override val critChanceRoll = 0.2 to 0.4
    override val critDamageRoll = 1.2 to 1.5
    override val offHandPenaltyRoll = -0.6 to -0.5

    override val range = 1
    override val hands = 1

    override fun rollStats(rarityModifier: Double, baseRoll: Double) {
        super.rollStats(rarityModifier, baseRoll)
        addModifier(ModifierType.DODGE, perfectOrRandom(1.0 to 1.25))
        addModifier(ModifierType.INCOMING_PHYSICAL_DAMAGE, perfectOrRandom(0.8 to 1.0))
    }
}