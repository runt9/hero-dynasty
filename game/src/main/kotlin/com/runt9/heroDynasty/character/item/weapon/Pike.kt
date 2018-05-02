package com.runt9.heroDynasty.character.item.weapon

class Pike : Weapon() {
    override val damageRangeRoll = 0.95 to 1.25
    override val damageClampRoll = 0.07 to 0.11
    override val accuracyRoll = 0.7 to 0.9
    override val critChanceRoll = 1.0 to 1.3
    override val critDamageRoll = 1.0 to 1.25

    override val range = 2
    override val hands = 2
}