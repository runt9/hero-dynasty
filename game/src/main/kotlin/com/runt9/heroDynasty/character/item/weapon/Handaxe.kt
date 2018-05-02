package com.runt9.heroDynasty.character.item.weapon

class Handaxe : Weapon() {
    override val damageRangeRoll = 0.7 to 1.15
    override val damageClampRoll = 0.11 to 0.3
    override val accuracyRoll = 0.7 to 0.9
    override val critChanceRoll = 0.9 to 1.3
    override val critDamageRoll = 1.2 to 1.7
    override val offHandPenaltyRoll = -0.6 to -0.2

    override val range = 1
    override val hands = 1
}