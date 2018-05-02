package com.runt9.heroDynasty.character.item.armor

class Mail : Armor() {
    override val dodgeChanceRoll: Pair<Double, Double> = 0.95 to 0.99
    override val physicalIncomingDamageRoll: Pair<Double, Double> = 0.92 to 0.97
    override val insulatedIncomingDamageRoll: Pair<Double, Double> = 0.98 to 1.0

    override fun getNameSuffixes(): List<String> {
        val nameSuffixes = super.getNameSuffixes().toMutableList()

        val positionSuffix = when (position) {
            Armor.Position.HEAD -> "Helmet"
            Armor.Position.BODY -> "Coat"
            Armor.Position.LEGS -> "Pants"
            Armor.Position.ARMS -> "Gloves"
            Armor.Position.FEET -> "Boots"
        }

        nameSuffixes.add(positionSuffix)
        return nameSuffixes.toList()
    }
}