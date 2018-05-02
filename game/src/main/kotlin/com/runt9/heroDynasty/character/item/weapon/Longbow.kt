package com.runt9.heroDynasty.character.item.weapon

class Longbow : Weapon() {
    override val damageRangeRoll = 0.7 to 1.3
    override val damageClampRoll = 0.1 to 0.25
    override val accuracyRoll = 0.8 to 1.1
    override val critChanceRoll = 0.9 to 1.1
    override val critDamageRoll = 1.1 to 1.4

    override val range = 6
    override val hands = 2
}