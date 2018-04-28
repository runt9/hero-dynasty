package com.runt9.heroDynasty.character.item.weapon

class Shield : Weapon() {
    override val damageRangeRoll = Pair(0.4, 0.65)
    override val damageClampRoll = Pair(0.04, 0.12)
    override val accuracyRoll = Pair(1.2, 1.4)
    override val critChanceRoll = Pair(0.2, 0.4)
    override val critDamageRoll = Pair(1.2, 1.5)
    override val offHandPenaltyRoll = Pair(-0.6, -0.5)

    override val range = 1
    override val hands = 1

    // TODO: Add special abilities for dodge chance
}