package com.runt9.heroDynasty.character.race

import com.runt9.heroDynasty.character.attribute.Attributes

class Draconian : Race() {
    override val description = """
    """.trimIndent()

    override val worldMapActionsPerTurn = 3
    override val skillPointsPerLevel = 2
    override val baseHitPointModifier = 1.4
    override val attributes = Attributes(2, 7, -6, -4)
}