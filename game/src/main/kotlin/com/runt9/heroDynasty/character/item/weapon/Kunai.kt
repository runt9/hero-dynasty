package com.runt9.heroDynasty.character.item.weapon

class Kunai : Weapon() {
    override val damageRangeRoll = Pair(0.8, 1.05)
    override val damageClampRoll = Pair(0.05, 0.08)
    override val accuracyRoll = Pair(0.75, 0.9)
    override val critChanceRoll = Pair(0.6, 1.0)
    override val critDamageRoll = Pair(1.1, 1.4)
    override val offHandPenaltyRoll = Pair(-0.4, -0.2)

    override val range = 3
    override val hands = 1
}