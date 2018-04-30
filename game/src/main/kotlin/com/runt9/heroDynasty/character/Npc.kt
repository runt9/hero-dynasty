package com.runt9.heroDynasty.character

class Npc(val powerLevel: NpcPowerLevel, override val name: String) : Character(name) {
    init {
        hitPoints.base = 100 * powerLevel.hp
        hitPoints.recalculate(1)
        addModifiers(ModifierType.ALL_DAMAGE to powerLevel.damage, ModifierType.HP_PER_LEVEL to powerLevel.hp)
    }
}