package com.runt9.heroDynasty.character.item.weapon

class Longsword : Weapon() {
    override val damageRangeRoll = 0.9 to 1.35
    override val damageClampRoll = 0.09 to 0.25
    override val accuracyRoll = 0.85 to 1.0
    override val critChanceRoll = 0.7 to 0.9
    override val critDamageRoll = 1.2 to 1.5

    override val range = 1
    override val hands = 2
}