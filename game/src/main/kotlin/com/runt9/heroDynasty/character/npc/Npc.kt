package com.runt9.heroDynasty.character.npc

import com.runt9.heroDynasty.character.Character
import com.runt9.heroDynasty.character.Modifier
import com.runt9.heroDynasty.character.ModifierType

class Npc(val powerLevel: NpcPowerLevel, override val name: String) : Character(name) {
    init {
        hitPoints.base = 100 * powerLevel.hp
        addModifiers(Modifier(ModifierType.ALL_DAMAGE, powerLevel.damage), Modifier(ModifierType.HP_PER_LEVEL, powerLevel.hp))
        hitPoints.recalculate(1)
    }
}