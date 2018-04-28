package com.runt9.heroDynasty.character.item.weapon

class Shortsword : Weapon() {
    override val damageRangeRoll = Pair(0.8, 1.1)
    override val damageClampRoll = Pair(0.08, 0.2)
    override val accuracyRoll = Pair(0.9, 1.0)
    override val critChanceRoll = Pair(0.8, 1.0)
    override val critDamageRoll = Pair(1.0, 1.3)
    override val offHandPenaltyRoll = Pair(-0.6, -0.3)

    override val range = 1
    override val hands = 1
}