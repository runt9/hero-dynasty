package com.runt9.heroDynasty.character.item

enum class Rarity(val dropModifier: Double, val perfectChance: Double) {
    COMMON(1.0, 0.05),
    UNCOMMON(0.5, 0.025),
    RARE(0.1, 0.01),
    VERY_RARE(0.025, 0.001),
    UNIQUE(0.01, 0.0)
}