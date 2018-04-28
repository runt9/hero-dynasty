package com.runt9.heroDynasty.character.item.armor

class Mail : Armor() {
    override val dodgeChanceRoll: Pair<Double, Double> = Pair(0.95, 0.99)
    override val physicalIncomingDamageRoll: Pair<Double, Double> = Pair(0.92, 0.97)
    override val insulatedIncomingDamageRoll: Pair<Double, Double> = Pair(0.98, 1.0)
}