package com.runt9.heroDynasty.character.item.weapon

class Warhammer : Weapon() {
    override val damageRangeRoll = Pair(0.9, 1.4)
    override val damageClampRoll = Pair(0.06, 0.21)
    override val accuracyRoll = Pair(0.8, 0.9)
    override val critChanceRoll = Pair(0.6, 0.8)
    override val critDamageRoll = Pair(1.2, 1.7)

    override val range = 1
    override val hands = 2
}