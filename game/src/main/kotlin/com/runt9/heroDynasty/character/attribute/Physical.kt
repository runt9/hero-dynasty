package com.runt9.heroDynasty.character.attribute

import com.runt9.heroDynasty.character.Modifier
import com.runt9.heroDynasty.character.ModifierType

class Physical(override var value: Int = 0) : Attribute(value) {
    override fun getModifiers(): List<Modifier> = mutableListOf(
            Modifier(ModifierType.PHYSICAL_DAMAGE, 1 + (value * 0.025)),
            Modifier(ModifierType.CRIT_DAMAGE, 1 + (value * 0.05)),
            Modifier(ModifierType.HP_PER_LEVEL, 1 + (value * 0.05)),
            Modifier(ModifierType.ACTION_COST, 1 + (value * -0.01)),
            Modifier(ModifierType.DODGE, 1 + (value * 0.0125))
    )
}