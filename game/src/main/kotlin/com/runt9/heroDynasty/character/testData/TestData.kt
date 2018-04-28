package com.runt9.heroDynasty.character.testData

import com.runt9.heroDynasty.character.ModifierType
import com.runt9.heroDynasty.character.Npc
import com.runt9.heroDynasty.character.NpcPowerLevel
import com.runt9.heroDynasty.character.Player
import com.runt9.heroDynasty.character.item.weapon.BattleAxe
import com.runt9.heroDynasty.character.race.Human

// TODO: Once race DSL is done, have list of races and pick a random one
val testPlayer = run {
    val player = Player(Human())
    player.inventory.primaryHand = BattleAxe()
    player.recalculateModifiers()
    player.addModifier(ModifierType.PHYSICAL_DAMAGE, 1.5)
    return@run player
}

val testEnemies = (0 until 20).map {
    val powerLevel = when {
        it % 2 == 0 -> NpcPowerLevel.MINION
        it % 5 == 0 -> NpcPowerLevel.GUARD
        it % 20 == 0 -> NpcPowerLevel.BOSS
        else -> NpcPowerLevel.CREATURE
    }

    return@map Npc(powerLevel)
}