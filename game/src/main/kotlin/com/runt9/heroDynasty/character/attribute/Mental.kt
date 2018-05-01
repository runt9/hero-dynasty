package com.runt9.heroDynasty.character.attribute

import com.runt9.heroDynasty.character.Modifier
import com.runt9.heroDynasty.character.ModifierType

class Mental(override var value: Int) : Attribute(value) {
    override fun getModifiers(): List<Modifier> = mutableListOf(
            Modifier(ModifierType.MENTAL_DAMAGE, 1 + (value * 0.025)),
            Modifier(ModifierType.ACCURACY, 1 + (value * 0.0125)),
            Modifier(ModifierType.CRIT_CHANCE, 1 + (value * 0.025)),
            Modifier(ModifierType.HP_REGEN, 1 + (value * 0.05)),
            Modifier(ModifierType.SKILL_POINTS_PER_LEVEL, 1 + (value * 0.05)),
            Modifier(ModifierType.COOLDOWN, 1 + (value * -0.0125))
    )
}