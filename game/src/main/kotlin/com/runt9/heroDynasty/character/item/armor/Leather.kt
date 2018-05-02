package com.runt9.heroDynasty.character.item.armor

class Leather : Armor() {
    override val dodgeChanceRoll: Pair<Double, Double> = 0.97 to 1.0
    override val physicalIncomingDamageRoll: Pair<Double, Double> = 0.96 to 0.99
    override val insulatedIncomingDamageRoll: Pair<Double, Double> = 0.95 to 0.98

    override fun getNameSuffixes(): List<String> {
        val nameSuffixes = super.getNameSuffixes().toMutableList()

        val positionSuffix = when (position) {
            Armor.Position.HEAD -> "Cap"
            Armor.Position.BODY -> "Jacket"
            Armor.Position.LEGS -> "Pants"
            Armor.Position.ARMS -> "Gloves"
            Armor.Position.FEET -> "Boots"
        }

        nameSuffixes.add(positionSuffix)
        return nameSuffixes.toList()
    }
}