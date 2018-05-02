package com.runt9.heroDynasty.character.item.weapon

class Warhammer : Weapon() {
    override val damageRangeRoll = 0.9 to 1.4
    override val damageClampRoll = 0.06 to 0.21
    override val accuracyRoll = 0.8 to 0.9
    override val critChanceRoll = 0.6 to 0.8
    override val critDamageRoll = 1.2 to 1.7

    override val range = 1
    override val hands = 2
}