package com.runt9.heroDynasty.character.item.weapon

class Longsword : Weapon() {
    override val damageRangeRoll = Pair(0.9, 1.35)
    override val damageClampRoll = Pair(0.09, 0.25)
    override val accuracyRoll = Pair(0.85, 1.0)
    override val critChanceRoll = Pair(0.7, 0.9)
    override val critDamageRoll = Pair(1.2, 1.5)

    override val range = 1
    override val hands = 2
}