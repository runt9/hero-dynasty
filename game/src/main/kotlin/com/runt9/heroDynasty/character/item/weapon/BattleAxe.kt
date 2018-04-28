package com.runt9.heroDynasty.character.item.weapon

class BattleAxe : Weapon() {
    override val damageRangeRoll = Pair(0.85, 1.5)
    override val damageClampRoll = Pair(0.13, 0.3)
    override val accuracyRoll = Pair(0.65, 0.85)
    override val critChanceRoll = Pair(1.0, 1.5)
    override val critDamageRoll = Pair(1.5, 2.0)

    override val range = 1
    override val hands = 2
}