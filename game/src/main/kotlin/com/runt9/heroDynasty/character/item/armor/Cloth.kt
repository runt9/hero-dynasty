package com.runt9.heroDynasty.character.item.armor

class Cloth : Armor() {
    override val dodgeChanceRoll: Pair<Double, Double> = Pair(0.98, 1.05)
    override val physicalIncomingDamageRoll: Pair<Double, Double> = Pair(0.99, 1.0)
    override val insulatedIncomingDamageRoll: Pair<Double, Double> = Pair(0.97, 0.99)
}