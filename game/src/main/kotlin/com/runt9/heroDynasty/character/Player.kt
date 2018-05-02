package com.runt9.heroDynasty.character

import com.runt9.heroDynasty.character.item.Item
import com.runt9.heroDynasty.character.race.Race
import com.runt9.heroDynasty.core.random
import kotlin.math.roundToInt

class Player(val race: Race, override val name: String) : Character(name) {
    val resources = mutableListOf<Resource>()
    val attributes = race.attributes.copy() // Copy instead of clone so we don't taint the race

    var xp = 0
    var xpToNextLevel = calculateXpToNextLevel()
    var skillsPerLevel = race.skillPointsPerLevel
    // TODO: Handle skill points spent to calculate current skill points on SPL change

    var attributePoints = 6
    var skillPoints = 2 + skillsPerLevel

    val itemVault = mutableListOf<Item>() // TODO: This will be part of the faction

    init {
        hitPoints.base = 100 * race.baseHitPointModifier
        hitPoints.recalculate(level)
    }

    private fun calculateXpToNextLevel()= (100 * Math.pow(level.toDouble(), 2.0)).toInt()

    private fun levelUp() {
        level++
        xp = 0
        xpToNextLevel = calculateXpToNextLevel()
        hitPoints.recalculate(level)
        hitPoints.current = hitPoints.max // Heal to full on level up
        skillPoints += skillsPerLevel + (if (level % 5 == 0) 1 else 0)
        attributePoints += 2 + (if (level % 5 == 0) 1 else 0)
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

    fun gainGold(gold: Pair<Double, Double>): Double {
        val goldAdded = 10 * gold.random() * getModifier(ModifierType.GOLD_DROP)
        inventory.gold += goldAdded
        return goldAdded
    }

    override fun recalculateModifiers() {
        super.recalculateModifiers()
        addModifiers(attributes.getModifiers())
    }

    override fun applyModifiers(childCheck: ((Modifier) -> Unit)?) {
        super.applyModifiers { mod ->
            if (mod.type == ModifierType.SKILL_POINTS_PER_LEVEL) {
                skillsPerLevel = (race.skillPointsPerLevel * mod.value).roundToInt()
            }
        }
    }
}