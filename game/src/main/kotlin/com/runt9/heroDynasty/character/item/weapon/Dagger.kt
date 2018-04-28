package com.runt9.heroDynasty.character.item.weapon

class Dagger : Weapon() {
    override val damageRangeRoll = Pair(0.7, 0.9)
    override val damageClampRoll = Pair(0.02, 0.08)
    override val accuracyRoll = Pair(1.1, 1.25)
    override val critChanceRoll = Pair(1.0, 1.25)
    override val critDamageRoll = Pair(0.7, 1.0)
    override val offHandPenaltyRoll = Pair(-0.3, -0.1)

    override val range = 1
    override val hands = 1
}