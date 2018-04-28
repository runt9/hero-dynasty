package com.runt9.heroDynasty.character.item.weapon

class Spear : Weapon() {
    override val damageRangeRoll = Pair(0.75, 0.95)
    override val damageClampRoll = Pair(0.05, 0.15)
    override val accuracyRoll = Pair(0.8, 1.0)
    override val critChanceRoll = Pair(0.8, 1.0)
    override val critDamageRoll = Pair(1.1, 1.4)
    override val offHandPenaltyRoll = Pair(-0.8, -0.6)

    override val range = 2
    override val hands = 1
}