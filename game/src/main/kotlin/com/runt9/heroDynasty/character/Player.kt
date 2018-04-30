package com.runt9.heroDynasty.character

import com.runt9.heroDynasty.character.race.Race
import com.runt9.heroDynasty.core.random

class Player(val race: Race) : Character() {
    val resources = mutableListOf<Resource>()
    val attributes = race.attributes.copy() // Copy instead of clone so we don't taint the

    // Morality
    var goodness = 0.0
    var consistency = 0.0

    var xp = 0
    var xpToNextLevel = calculateXpToNextLevel()
    var skillsPerLevel = race.skillPointsPerLevel

    init {
        hitPoints.base = 100 * race.baseHitPointModifier
        hitPoints.recalculate(level)
    }

    private fun calculateXpToNextLevel()= (100 * Math.pow(level.toDouble(), 2.0)).toInt()

    private fun levelUp() {
        level++
        xp = 0
        xpToNextLevel = calculateXpToNextLevel()
        hitPoints.current = hitPoints.max // Heal to full on level up
        hitPoints.recalculate(level)

        // TODO: Skills and attributes
    }

    fun gainExperience(xp: Int): Boolean {
        var levelledUp = false
        this.xp += xp
        if (this.xp >= xpToNextLevel) {
            levelledUp = true
            val extraXp = this.xp - xpToNextLevel
            levelUp()
            gainExperience(extraXp)
        }

        calculateXpToNextLevel()

        return levelledUp
    }

    fun gainGold(gold: Pair<Double, Double>) {
        inventory.gold += 10 * gold.random()
    }
}