package com.runt9.heroDynasty.character.race

import com.runt9.heroDynasty.character.attribute.Attributes

class Skeleton : Race() {
    override val name = "Skeleton"
    override val description = """
    """.trimIndent()

    override val worldMapActionsPerTurn = 1
    override val skillPointsPerLevel = 2
    override val baseHitPointModifier = 0.9
    override val attributes = Attributes(4, -3, 0, -4, 2)
}