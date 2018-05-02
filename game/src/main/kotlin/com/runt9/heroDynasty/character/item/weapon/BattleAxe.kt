package com.runt9.heroDynasty.character.item.weapon

class BattleAxe : Weapon() {
    override val damageRangeRoll = 0.85 to 1.5
    override val damageClampRoll = 0.13 to 0.3
    override val accuracyRoll = 0.65 to 0.85
    override val critChanceRoll = 1.0 to 1.5
    override val critDamageRoll = 1.5 to 2.0

    override val range = 1
    override val hands = 2
}