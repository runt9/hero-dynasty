package com.runt9.heroDynasty.character.item.weapon

class HandCrossbow : Weapon() {
    override val damageRangeRoll = Pair(0.6, 1.0)
    override val damageClampRoll = Pair(0.04, 0.12)
    override val accuracyRoll = Pair(0.9, 1.15)
    override val critChanceRoll = Pair(1.1, 1.3)
    override val critDamageRoll = Pair(0.9, 1.0)
    override val offHandPenaltyRoll = Pair(-0.5, -0.3)

    override val range = 4
    override val hands = 1
}