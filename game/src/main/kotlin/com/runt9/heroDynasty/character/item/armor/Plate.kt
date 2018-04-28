package com.runt9.heroDynasty.character.item.armor

class Plate : Armor() {
    override val dodgeChanceRoll: Pair<Double, Double> = Pair(0.9, 0.93)
    override val physicalIncomingDamageRoll: Pair<Double, Double> = Pair(0.88, 0.91)
    override val insulatedIncomingDamageRoll: Pair<Double, Double> = Pair(0.96, 1.0)
}