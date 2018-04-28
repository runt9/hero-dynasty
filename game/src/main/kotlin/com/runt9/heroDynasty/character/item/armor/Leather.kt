package com.runt9.heroDynasty.character.item.armor

class Leather : Armor() {
    override val dodgeChanceRoll: Pair<Double, Double> = Pair(0.97, 1.0)
    override val physicalIncomingDamageRoll: Pair<Double, Double> = Pair(0.96, 0.99)
    override val insulatedIncomingDamageRoll: Pair<Double, Double> = Pair(0.95, 0.98)
}