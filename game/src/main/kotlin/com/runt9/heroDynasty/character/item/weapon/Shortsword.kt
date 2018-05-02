package com.runt9.heroDynasty.character.item.weapon

class Shortsword : Weapon() {
    override val damageRangeRoll = 0.8 to 1.1
    override val damageClampRoll = 0.08 to 0.2
    override val accuracyRoll = 0.9 to 1.0
    override val critChanceRoll = 0.8 to 1.0
    override val critDamageRoll = 1.0 to 1.3
    override val offHandPenaltyRoll = -0.6 to -0.3

    override val range = 1
    override val hands = 1
}