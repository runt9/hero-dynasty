package com.runt9.heroDynasty.character

import com.runt9.heroDynasty.character.race.Race

class Player(val race: Race) : Character() {
    val resources = mutableListOf<Resource>()
    val attributes = race.attributes.copy() // Copy instead of clone so we don't taint the

    // Morality
    var goodness = 0.0
    var consistency = 0.0

    var experience = 0
    var skillsPerLevel = race.skillPointsPerLevel

    init {
        hitPoints.base = 100 * race.baseHitPointModifier
        hitPoints.recalculate(level)
    }
}