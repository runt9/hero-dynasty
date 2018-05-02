package com.runt9.heroDynasty.character.item.weapon

class Dagger : Weapon() {
    override val damageRangeRoll = 0.7 to 0.9
    override val damageClampRoll = 0.02 to 0.08
    override val accuracyRoll = 1.1 to 1.25
    override val critChanceRoll = 1.0 to 1.25
    override val critDamageRoll = 0.7 to 1.0
    override val offHandPenaltyRoll = -0.3 to -0.1

    override val range = 1
    override val hands = 1
}