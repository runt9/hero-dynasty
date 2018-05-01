package com.runt9.heroDynasty.character

class Npc(val powerLevel: NpcPowerLevel, override val name: String) : Character(name) {
    init {
        hitPoints.base = 100 * powerLevel.hp
        addModifiers(Modifier(ModifierType.ALL_DAMAGE, powerLevel.damage), Modifier(ModifierType.HP_PER_LEVEL, powerLevel.hp))
        hitPoints.recalculate(1)
    }
}