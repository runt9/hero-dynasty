package com.runt9.heroDynasty.character.item.armor

import com.runt9.heroDynasty.character.Modifier
import com.runt9.heroDynasty.character.ModifierType
import com.runt9.heroDynasty.character.item.Item
import com.runt9.heroDynasty.core.randomChance
import com.runt9.heroDynasty.core.randomEnum

abstract class Armor : Item() {
    enum class Position { HEAD, BODY, LEGS, ARMS, FEET }

    protected abstract val dodgeChanceRoll: Pair<Double, Double>
    protected abstract val physicalIncomingDamageRoll: Pair<Double, Double>
    protected abstract val insulatedIncomingDamageRoll: Pair<Double, Double>

    private lateinit var dodgeChance: Modifier
    private lateinit var incomingPhysicalDamage: Modifier
    // 10% chance for insulated armor
    private var insulatedIncomingDamage: List<Modifier> = listOf()
    private val isInsulated get() = insulatedIncomingDamage.isNotEmpty()

    lateinit var position: Position

    override fun rollStats(rarityModifier: Double, baseRoll: Double) {
        super.rollStats(rarityModifier, baseRoll)
        position = randomEnum(Position::class)
        addModifier(ModifierType.DODGE, perfectOrRandom(dodgeChanceRoll))
        addModifier(ModifierType.INCOMING_PHYSICAL_DAMAGE, perfectOrRandom(physicalIncomingDamageRoll))

        if (randomChance(multiplier = 0.1).success) {
            val insulatedRoll = perfectOrRandom(insulatedIncomingDamageRoll)

            addModifier(ModifierType.INCOMING_COLD_DAMAGE, insulatedRoll)
            addModifier(ModifierType.INCOMING_FIRE_DAMAGE, insulatedRoll)
            addModifier(ModifierType.INCOMING_ENERGY_DAMAGE, insulatedRoll)
        }
    }

    override fun getNamePrefixes(): List<String> {
        val prefixes = super.getNamePrefixes().toMutableList()

        if (isInsulated) {
            prefixes.add("Insulated")
        }

        return prefixes.toList()
    }
}