package com.runt9.heroDynasty.character.item.weapon

class Quarterstaff : Weapon() {
    override val damageRangeRoll = 0.7 to 1.2
    override val damageClampRoll = 0.15 to 0.4
    override val accuracyRoll = 0.8 to 1.0
    override val critChanceRoll = 0.4 to 0.6
    override val critDamageRoll = 1.1 to 1.3

    override val range = 1
    override val hands = 2
}