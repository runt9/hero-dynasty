package com.runt9.heroDynasty.character.race

import com.runt9.heroDynasty.character.attribute.Attributes

abstract class Race {
    abstract val description: String

    abstract val worldMapActionsPerTurn: Int
    abstract val skillPointsPerLevel: Int
    abstract val baseHitPointModifier: Double
    abstract val attributes: Attributes

//    abstract val abilities: List<Ability>
//    abstract val enchantments: List<Enchantment>
}