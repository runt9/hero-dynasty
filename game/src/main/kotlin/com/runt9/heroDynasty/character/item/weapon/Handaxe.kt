package com.runt9.heroDynasty.character.item.weapon

class Handaxe : Weapon() {
    override val damageRangeRoll = Pair(0.7, 1.15)
    override val damageClampRoll = Pair(0.11, 0.3)
    override val accuracyRoll = Pair(0.7, 0.9)
    override val critChanceRoll = Pair(0.9, 1.3)
    override val critDamageRoll = Pair(1.2, 1.7)
    override val offHandPenaltyRoll = Pair(-0.6, -0.2)

    override val range = 1
    override val hands = 1
}