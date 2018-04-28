package com.runt9.heroDynasty.character.item.armor

import com.runt9.heroDynasty.character.item.Item
import com.runt9.heroDynasty.core.randomChance
import com.runt9.heroDynasty.core.randomEnum

abstract class Armor : Item() {
    enum class Position { HEAD, BODY, LEGS, ARMS, FEET }

    protected abstract val dodgeChanceRoll: Pair<Double, Double>
    protected abstract val physicalIncomingDamageRoll: Pair<Double, Double>
    protected abstract val insulatedIncomingDamageRoll: Pair<Double, Double>

    val dodgeChance by lazy { perfectOrRandom(dodgeChanceRoll) }
    val physicalIncomingDamage by lazy { perfectOrRandom(physicalIncomingDamageRoll) }
    // 10% chance for insulated armor
    val insulatedIncomingDamage by lazy { if (randomChance(0.1)) perfectOrRandom(insulatedIncomingDamageRoll) else 1.0 }
    val isInsulated get() = insulatedIncomingDamage != 1.0

    val position: Position by lazy { randomEnum(Position::class) }
}