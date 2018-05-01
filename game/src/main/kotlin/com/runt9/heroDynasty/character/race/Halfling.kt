package com.runt9.heroDynasty.character.race

import com.runt9.heroDynasty.character.attribute.Attributes


class Halfling : Race() {
    override val name = "Halfling"
    override val description = """
    """.trimIndent()

    override val worldMapActionsPerTurn = 4
    override val skillPointsPerLevel = 2
    override val baseHitPointModifier = 0.8
    override val attributes = Attributes(-1, 4, 2, 5)
}