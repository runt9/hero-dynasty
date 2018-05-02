package com.runt9.heroDynasty.character.item.armor

class Cloth : Armor() {
    override val dodgeChanceRoll: Pair<Double, Double> = 0.98 to 1.05
    override val physicalIncomingDamageRoll: Pair<Double, Double> = 0.99 to 1.0
    override val insulatedIncomingDamageRoll: Pair<Double, Double> = 0.97 to 0.99

    override fun getNameSuffixes(): List<String> {
        val nameSuffixes = super.getNameSuffixes().toMutableList()

        val positionSuffix = when (position) {
            Armor.Position.HEAD -> "Hat"
            Armor.Position.BODY -> "Shirt"
            Armor.Position.LEGS -> "Pants"
            Armor.Position.ARMS -> "Gloves"
            Armor.Position.FEET -> "Shoes"
        }

        nameSuffixes.add(positionSuffix)
        return nameSuffixes.toList()
    }
}