package com.runt9.heroDynasty.character.race

import com.runt9.heroDynasty.character.attribute.Attributes

class Human : Race() {
    override val name: String = "Human"
    override val description = """
        Humans are social creatures who are usually good at many things but rarely the best at any one thing
        (other than talking, boy do humans like to talk!).
        Humans are a good race to play to learn the game or to have a little more build flexibility as they can do just about anything.
    """.trimIndent()

    override val worldMapActionsPerTurn = 3
    override val skillPointsPerLevel = 4
    override val baseHitPointModifier = 1.0
    override val attributes = Attributes(1, 3, 0, 6, 1)
}