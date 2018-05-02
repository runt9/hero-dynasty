package com.runt9.heroDynasty.character

fun List<Modifier>.sum(): Double = this.sumByDouble { it.value } - (this.size - 1)

fun List<Modifier>.sum(vararg types: ModifierType): Double {
    val filtered = this.filter { types.contains(it.type) }
    return if (filtered.isEmpty()) 1.0 else filtered.sumByDouble { it.value } - (filtered.size - 1)
}

fun List<Modifier>.categorize(vararg categories: ModifierCategory) = when {
    categories.isEmpty() -> this.groupBy { it.type.category }
    else -> this.filter { categories.contains(it.type.category) }.groupBy { it.type.category }
}

fun List<Modifier>.groupByType(): List<Modifier> = this.groupBy { it.type }.map { Modifier(it.key, it.value.sum()) }

// TODO: Modifier source for tooltip display
data class Modifier(val type: ModifierType, val value: Double)

enum class ModifierCategory {
    ATTACK,
    DAMAGE,
    DEFENSE,
    INCOMING_DAMAGE,
    CRIT,
    SPEED,
    RESOURCE_POOL,
    RESOURCE_REGEN,
    LUCK,
    CHARACTER
}

enum class ModifierType(val category: ModifierCategory, val displayName: String) {
    // Damage
    ALL_DAMAGE(ModifierCategory.DAMAGE, "All"),
    SPELL_DAMAGE(ModifierCategory.DAMAGE, "Spell"),
    PHYSICAL_DAMAGE(ModifierCategory.DAMAGE, "Physical"),
    MENTAL_DAMAGE(ModifierCategory.DAMAGE, "Mental"),
    POISON_DAMAGE(ModifierCategory.DAMAGE, "Poison"),
    FIRE_DAMAGE(ModifierCategory.DAMAGE, "Fire"),
    COLD_DAMAGE(ModifierCategory.DAMAGE, "Cold"),
    ENERGY_DAMAGE(ModifierCategory.DAMAGE, "Energy"),
    LIGHT_DAMAGE(ModifierCategory.DAMAGE, "Light"),
    DARK_DAMAGE(ModifierCategory.DAMAGE, "Dark"),

    // NB: Probably will end up wanting one for each damage type, but let's go with this for now
    INCOMING_DAMAGE_PENETRATION(ModifierCategory.DAMAGE, "Penetration"),

    // Damage Reduction
    INCOMING_ALL_DAMAGE(ModifierCategory.INCOMING_DAMAGE, "All"),
    INCOMING_PHYSICAL_DAMAGE(ModifierCategory.INCOMING_DAMAGE, "Physical"),
    INCOMING_MENTAL_DAMAGE(ModifierCategory.INCOMING_DAMAGE, "Mental"),
    INCOMING_POISON_DAMAGE(ModifierCategory.INCOMING_DAMAGE, "Poison"),
    INCOMING_FIRE_DAMAGE(ModifierCategory.INCOMING_DAMAGE, "Fire"),
    INCOMING_COLD_DAMAGE(ModifierCategory.INCOMING_DAMAGE, "Cold"),
    INCOMING_ENERGY_DAMAGE(ModifierCategory.INCOMING_DAMAGE, "Energy"),
    INCOMING_LIGHT_DAMAGE(ModifierCategory.INCOMING_DAMAGE, "Light"),
    INCOMING_DARK_DAMAGE(ModifierCategory.INCOMING_DAMAGE, "Dark"),

    // TODO: Spell/ability effectiveness?

    // Crit
    CRIT_CHANCE(ModifierCategory.CRIT, "Crit Chance"),
    CRIT_DAMAGE(ModifierCategory.CRIT, "Crit Damage"),

    ACTION_COST(ModifierCategory.SPEED, "Action Cost"),
    DODGE(ModifierCategory.DEFENSE, "Dodge Chance"),

    // Resources
    HP_PER_LEVEL(ModifierCategory.RESOURCE_POOL, "HP / lvl"),
    HP_REGEN(ModifierCategory.RESOURCE_REGEN, "HP Regen"),

    ACCURACY(ModifierCategory.ATTACK, "Accuracy"),
    SKILL_POINTS_PER_LEVEL(ModifierCategory.CHARACTER, "Skills per Level"),
    COOLDOWN(ModifierCategory.SPEED, "Cooldowns"),

    // Luck
    RARITY_FIND(ModifierCategory.LUCK, "Rarity Find"),
    GOLD_DROP(ModifierCategory.LUCK, "Gold Drop"),
    DROP_CHANCE(ModifierCategory.LUCK, "Item Drop Chance")
}