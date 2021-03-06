package com.runt9.heroDynasty.character.attribute

import com.runt9.heroDynasty.character.Modifier
import com.runt9.heroDynasty.character.ModifierType

class Luck(override var value: Int) : Attribute(value) {
    override fun getModifiers(): List<Modifier> = mutableListOf(
            Modifier(ModifierType.ACCURACY, 1 + (value * 0.0075)),
            Modifier(ModifierType.DODGE, 1 + (value * 0.0075)),
            Modifier(ModifierType.CRIT_CHANCE, 1 + (value * 0.0075)),
            Modifier(ModifierType.DROP_CHANCE, 1 + (value * 0.0125)),
            Modifier(ModifierType.RARITY_FIND, value * 0.002), // 0 base, gets added straight-up to regular drop roll
            Modifier(ModifierType.GOLD_DROP, 1 + (value * 0.025))
    )
}