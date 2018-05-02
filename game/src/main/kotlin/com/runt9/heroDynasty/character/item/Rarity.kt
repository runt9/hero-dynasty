package com.runt9.heroDynasty.character.item

import com.badlogic.gdx.graphics.Color

enum class Rarity(val dropModifier: Double, val perfectChance: Double, val color: Color) {
    COMMON(1.0, 0.05, Color.WHITE),
    UNCOMMON(0.5, 0.025, Color.CYAN),
    RARE(0.1, 0.01, Color.ORANGE),
    VERY_RARE(0.025, 0.001, Color.PURPLE),
    UNIQUE(0.01, 0.0, Color.YELLOW)
}