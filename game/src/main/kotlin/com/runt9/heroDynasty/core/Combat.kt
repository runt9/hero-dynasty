package com.runt9.heroDynasty.core

import com.runt9.heroDynasty.character.Character
import com.runt9.heroDynasty.character.ModifierType


const val baseDamage = 10
val unarmedDamageRange = Pair(0.25, 0.35)
val accuracyClamp = Pair(5.0, 95.0)
val incomingDamageClamp = Pair(0.1, 2.0)

fun basicAttack(attacker: Character, defender: Character): Double {
    // TODO: Dual-wielding

    val accuracy = attacker.getModifier(ModifierType.ACCURACY)
    val dodge = defender.getModifier(ModifierType.DODGE)

    // Attack roll
    val attackRollData = randomChance(1 + (accuracy - dodge), accuracyClamp)
    if (!attackRollData.success) return 0.0

    // Damage roll
    // TODO: Factor in abilities that add additional damage
    val damageMultiplier = attacker.getModifier(ModifierType.ALL_DAMAGE, ModifierType.PHYSICAL_DAMAGE)
    val damagePenetration = attacker.getModifier(ModifierType.INCOMING_DAMAGE_PENETRATION)
    val incomingDamageMultiplier = defender.getModifier(ModifierType.INCOMING_ALL_DAMAGE, ModifierType.INCOMING_PHYSICAL_DAMAGE, clampVal = incomingDamageClamp)
    // E.X. 1.1x damage multiplier, 0.7x incoming damage multiplier, 1.2x damage penetration = 1x final multiplier
    // 1.1 + (0.7 + 1.2 - 1) - 1
    // 1.1 + (0.9) - 1
    // 2 - 1 = 1
    val finalDamageMultiplier = damageMultiplier + ((incomingDamageMultiplier + (damagePenetration - 1)) - 1)
    val damageRoll = baseDamage * attacker.level * finalDamageMultiplier * attacker.getWeaponDamageRange().random()

    val critThreshold = attackRollData.rollSuccessAmount / 2 * attacker.getModifier(ModifierType.CRIT_CHANCE)
    val critDamage = attacker.getModifier(ModifierType.CRIT_DAMAGE)
    val finalDamage = if (randomChance(critThreshold)) damageRoll * critDamage else damageRoll
    defender.hitPoints.current -= finalDamage
    return finalDamage
}