package com.runt9.heroDynasty.character

class Npc(val powerLevel: NpcPowerLevel) : Character() {
    init {
        hitPoints.base = 100 * powerLevel.hp
        hitPoints.recalculate(1)
        addModifiers(ModifierType.ALL_DAMAGE to powerLevel.damage, ModifierType.HIT_POINTS_PER_LEVEL to powerLevel.hp)
    }
}