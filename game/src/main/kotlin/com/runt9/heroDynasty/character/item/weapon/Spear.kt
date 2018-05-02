package com.runt9.heroDynasty.character.item.weapon

class Spear : Weapon() {
    override val damageRangeRoll = 0.75 to 0.95
    override val damageClampRoll = 0.05 to 0.15
    override val accuracyRoll = 0.8 to 1.0
    override val critChanceRoll = 0.8 to 1.0
    override val critDamageRoll = 1.1 to 1.4
    override val offHandPenaltyRoll = -0.8 to -0.6

    override val range = 2
    override val hands = 1
}