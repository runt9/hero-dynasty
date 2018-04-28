package com.runt9.heroDynasty.character.item.weapon

class Shuriken : Weapon() {
    override val damageRangeRoll = Pair(0.65, 1.0)
    override val damageClampRoll = Pair(0.02, 0.07)
    override val accuracyRoll = Pair(0.9, 1.0)
    override val critChanceRoll = Pair(0.9, 1.1)
    override val critDamageRoll = Pair(0.9, 1.1)
    override val offHandPenaltyRoll = Pair(-0.2, -0.1)

    override val range = 4
    override val hands = 1
}