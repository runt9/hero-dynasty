package com.runt9.heroDynasty.character.race

import com.runt9.heroDynasty.character.attribute.Attributes

class Vampire : Race() {
    override val name = "Vampire"
    override val description = """
    """.trimIndent()

    override val worldMapActionsPerTurn = 2
    override val skillPointsPerLevel = 3
    override val baseHitPointModifier = 1.1
    override val attributes = Attributes(3, 1, 5, -1, -1)
}