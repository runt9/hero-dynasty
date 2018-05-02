package com.runt9.heroDynasty.character.npc

import com.badlogic.gdx.graphics.Color

enum class NpcPowerLevel(val damage: Double, val hp: Double, val xp: Double, val itemDrop: Double, val gold: Pair<Double, Double>, val color: Color) {
    CREATURE(0.2, 0.3, 0.1, 0.5, 0.0 to 0.1, Color.GREEN),
    MINION(0.5, 0.7, 0.25, 1.0, 0.0 to 0.5, Color.YELLOW),
    GUARD(1.0, 1.2, 0.5, 1.5, 0.25 to 1.25, Color.ORANGE),
    BOSS(1.5, 2.0, 3.0, 3.0, 0.75 to 2.5, Color.CYAN),
    HERO(0.0, 0.0, 8.0, 5.0, 1.0 to 5.0, Color.PURPLE)
}