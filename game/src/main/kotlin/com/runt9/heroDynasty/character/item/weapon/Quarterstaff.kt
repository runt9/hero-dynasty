package com.runt9.heroDynasty.character.item.weapon

class Quarterstaff : Weapon() {
    override val damageRangeRoll = Pair(0.7, 1.2)
    override val damageClampRoll = Pair(0.15, 0.4)
    override val accuracyRoll = Pair(0.8, 1.0)
    override val critChanceRoll = Pair(0.4, 0.6)
    override val critDamageRoll = Pair(1.1, 1.3)

    override val range = 1
    override val hands = 2
}