package com.runt9.heroDynasty.character.item.weapon

class RecurveBow : Weapon() {
    override val damageRangeRoll = Pair(0.9, 1.4)
    override val damageClampRoll = Pair(0.05, 0.2)
    override val accuracyRoll = Pair(0.6, 0.9)
    override val critChanceRoll = Pair(0.6, 0.9)
    override val critDamageRoll = Pair(1.3, 1.7)

    override val range = 8
    override val hands = 2
}