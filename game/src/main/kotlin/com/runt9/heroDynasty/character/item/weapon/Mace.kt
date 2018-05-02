package com.runt9.heroDynasty.character.item.weapon

class Mace : Weapon() {
    override val damageRangeRoll = 0.8 to 1.0
    override val damageClampRoll = 0.04 to 0.12
    override val accuracyRoll = 0.8 to 1.0
    override val critChanceRoll = 0.7 to 0.85
    override val critDamageRoll = 1.1 to 1.5
    override val offHandPenaltyRoll = -0.8 to -0.5

    override val range = 1
    override val hands = 1
}