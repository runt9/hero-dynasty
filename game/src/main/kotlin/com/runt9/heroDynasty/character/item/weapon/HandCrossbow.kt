package com.runt9.heroDynasty.character.item.weapon

class HandCrossbow : Weapon() {
    override val damageRangeRoll = 0.6 to 1.0
    override val damageClampRoll = 0.04 to 0.12
    override val accuracyRoll = 0.9 to 1.15
    override val critChanceRoll = 1.1 to 1.3
    override val critDamageRoll = 0.9 to 1.0
    override val offHandPenaltyRoll = -0.5 to -0.3

    override val range = 4
    override val hands = 1
}