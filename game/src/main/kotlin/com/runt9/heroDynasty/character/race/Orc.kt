package com.runt9.heroDynasty.character.race

import com.runt9.heroDynasty.character.attribute.Attributes

class Orc : Race() {
    override val name = "Orc"
    override val description = """
    """.trimIndent()

    override val worldMapActionsPerTurn = 1
    override val skillPointsPerLevel = 2
    override val baseHitPointModifier = 1.6
    override val attributes = Attributes(10, -3, -2, -1)
}