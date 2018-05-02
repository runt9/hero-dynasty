package com.runt9.heroDynasty.character.item.weapon

class RecurveBow : Weapon() {
    override val damageRangeRoll = 0.9 to 1.4
    override val damageClampRoll = 0.05 to 0.2
    override val accuracyRoll = 0.6 to 0.9
    override val critChanceRoll = 0.6 to 0.9
    override val critDamageRoll = 1.3 to 1.7

    override val range = 8
    override val hands = 2
}