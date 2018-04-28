package com.runt9.heroDynasty.character

enum class NpcPowerLevel(val damage: Double, val hp: Double, val xp: Double, val itemDrop: Double, val gold: Pair<Double, Double>) {
    CREATURE(0.2, 0.5, 0.1, 0.1, Pair(0.0, 0.1)),
    MINION(0.5, 1.0, 0.25, 0.5, Pair(0.0, 0.5)),
    GUARD(1.0, 1.5, 0.5, 1.0, Pair(0.25, 1.25)),
    BOSS(1.5, 2.0, 3.0, 2.0, Pair(0.75, 2.5)),
    HERO(0.0, 0.0, 8.0, 3.0, Pair(1.0, 5.0))
}