package com.runt9.heroDynasty.character.item.armor

class Plate : Armor() {
    override val dodgeChanceRoll: Pair<Double, Double> = 0.9 to 0.93
    override val physicalIncomingDamageRoll: Pair<Double, Double> = 0.88 to 0.91
    override val insulatedIncomingDamageRoll: Pair<Double, Double> = 0.96 to 1.0

    override fun getNamePrefixes(): List<String> {
        val namePrefixes = super.getNamePrefixes().toMutableList()

        if (position == Armor.Position.BODY) {
            namePrefixes.add("Chest")
        } else if (position == Armor.Position.LEGS) {
            namePrefixes.add("Leg")
        }

        return namePrefixes.toList()
    }

    override fun getNameSuffixes(): List<String> {
        val nameSuffixes = super.getNameSuffixes().toMutableList()

        val positionSuffix = when (position) {
            Armor.Position.HEAD -> "Helmet"
            Armor.Position.ARMS -> "Gauntlets"
            Armor.Position.FEET -> "Boots"
            else -> null
        }

        if (positionSuffix != null) {
            nameSuffixes.add(positionSuffix)
        }

        return nameSuffixes.toList()
    }
}