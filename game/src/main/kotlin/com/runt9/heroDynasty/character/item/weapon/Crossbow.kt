package com.runt9.heroDynasty.character.item.weapon

class Crossbow : Weapon() {
    override val damageRangeRoll = 0.7 to 1.2
    override val damageClampRoll = 0.02 to 0.1
    override val accuracyRoll = 1.0 to 1.2
    override val critChanceRoll = 1.0 to 1.2
    override val critDamageRoll = 1.0 to 1.2

    override val range = 5
    override val hands = 2
}