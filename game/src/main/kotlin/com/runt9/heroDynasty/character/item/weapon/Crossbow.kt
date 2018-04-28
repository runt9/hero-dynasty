package com.runt9.heroDynasty.character.item.weapon

class Crossbow : Weapon() {
    override val damageRangeRoll = Pair(0.7, 1.2)
    override val damageClampRoll = Pair(0.02, 0.1)
    override val accuracyRoll = Pair(1.0, 1.2)
    override val critChanceRoll = Pair(1.0, 1.2)
    override val critDamageRoll = Pair(1.0, 1.2)

    override val range = 5
    override val hands = 2
}