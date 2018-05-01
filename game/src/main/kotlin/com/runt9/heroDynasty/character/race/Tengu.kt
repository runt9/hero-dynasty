package com.runt9.heroDynasty.character.race

import com.runt9.heroDynasty.character.attribute.Attributes

class Tengu : Race() {
    override val name = "Tengu"
    override val description = """
    """.trimIndent()

    override val worldMapActionsPerTurn = 2
    override val skillPointsPerLevel = 3
    override val baseHitPointModifier = 0.8
    override val attributes = Attributes(-2, 5, 1, 2)
}