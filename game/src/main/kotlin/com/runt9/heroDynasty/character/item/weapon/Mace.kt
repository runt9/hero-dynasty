package com.runt9.heroDynasty.character.item.weapon

class Mace : Weapon() {
    override val damageRangeRoll = Pair(0.8, 1.0)
    override val damageClampRoll = Pair(0.04, 0.12)
    override val accuracyRoll = Pair(0.8, 1.0)
    override val critChanceRoll = Pair(0.7, 0.85)
    override val critDamageRoll = Pair(1.1, 1.5)
    override val offHandPenaltyRoll = Pair(-0.8, -0.5)

    override val range = 1
    override val hands = 1
}