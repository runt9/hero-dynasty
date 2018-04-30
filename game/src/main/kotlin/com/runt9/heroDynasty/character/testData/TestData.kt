package com.runt9.heroDynasty.character.testData

import com.runt9.heroDynasty.character.ModifierType
import com.runt9.heroDynasty.character.Npc
import com.runt9.heroDynasty.character.NpcPowerLevel
import com.runt9.heroDynasty.character.Player
import com.runt9.heroDynasty.character.item.weapon.BattleAxe
import com.runt9.heroDynasty.character.race.Human

// TODO: Once race DSL is done, have list of races and pick a random one
val testPlayer = run {
    val player = Player(Human(), "runt9")
    player.inventory.primaryHand = BattleAxe()
    player.recalculateModifiers()
    player.addModifier(ModifierType.PHYSICAL_DAMAGE, 1.5)
    return@run player
}

val testEnemies = (0 until 80).map {
    val powerLevel = when {
        it % 20 == 0 -> NpcPowerLevel.BOSS
        it % 5 == 0 -> NpcPowerLevel.GUARD
        it % 2 == 0 -> NpcPowerLevel.MINION
        else -> NpcPowerLevel.CREATURE
    }

    return@map Npc(powerLevel, "Enemy$it")
}