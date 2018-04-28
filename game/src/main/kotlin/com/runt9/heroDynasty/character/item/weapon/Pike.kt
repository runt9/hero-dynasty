package com.runt9.heroDynasty.character.item.weapon

class Pike : Weapon() {
    override val damageRangeRoll = Pair(0.95, 1.25)
    override val damageClampRoll = Pair(0.07, 0.11)
    override val accuracyRoll = Pair(0.7, 0.9)
    override val critChanceRoll = Pair(1.0, 1.3)
    override val critDamageRoll = Pair(1.0, 1.25)

    override val range = 2
    override val hands = 2
}