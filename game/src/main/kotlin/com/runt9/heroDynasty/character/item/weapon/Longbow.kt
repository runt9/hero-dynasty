package com.runt9.heroDynasty.character.item.weapon

class Longbow : Weapon() {
    override val damageRangeRoll = Pair(0.7, 1.3)
    override val damageClampRoll = Pair(0.1, 0.25)
    override val accuracyRoll = Pair(0.8, 1.1)
    override val critChanceRoll = Pair(0.9, 1.1)
    override val critDamageRoll = Pair(1.1, 1.4)

    override val range = 6
    override val hands = 2
}