package com.runt9.heroDynasty.character.race

import com.runt9.heroDynasty.character.attribute.Attributes

class Dwarf : Race() {
    override val name = "Dwarf"
    override val description = """
    """.trimIndent()

    override val worldMapActionsPerTurn = 2
    override val skillPointsPerLevel = 2
    override val baseHitPointModifier = 1.3
    override val attributes = Attributes(5, 2, -2, -1, 0)
}