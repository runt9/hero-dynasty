package com.runt9.heroDynasty.character.race

import com.runt9.heroDynasty.character.attribute.Attributes

class Elf : Race() {
    override val description = """
    """.trimIndent()

    override val worldMapActionsPerTurn = 3
    override val skillPointsPerLevel = 3
    override val baseHitPointModifier = 0.9
    override val attributes = Attributes(0, 4, 3, 2)
}