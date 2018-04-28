package com.runt9.heroDynasty.character.race

import com.runt9.heroDynasty.character.attribute.Attributes

// TODO: Refactor to builder DSL since it's all static data
abstract class Race {
    abstract val name: String
    abstract val description: String

    abstract val worldMapActionsPerTurn: Int
    abstract val skillPointsPerLevel: Int
    abstract val baseHitPointModifier: Double
    abstract val attributes: Attributes

//    abstract val abilities: List<Ability>
//    abstract val enchantments: List<Enchantment>
}