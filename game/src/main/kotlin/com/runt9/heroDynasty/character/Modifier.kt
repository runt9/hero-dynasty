package com.runt9.heroDynasty.character

fun List<Modifier>.sum(): Double = this.sumByDouble { it.value } - (this.size - 1)

fun List<Modifier>.sum(vararg types: ModifierType): Double {
    val filtered = this.filter { types.contains(it.type) }
    return if (filtered.isEmpty()) 1.0 else filtered.sumByDouble { it.value } - (filtered.size - 1)
}

fun List<Modifier>.categorize(vararg categories: ModifierCategory): Map<ModifierCategory, List<Modifier>> =
        this.filter { categories.contains(it.type.category) }.groupBy { it.type.category }

fun List<Modifier>.groupByType(): List<Modifier> = this.groupBy { it.type }.map { Modifier(it.key, it.value.sum()) }

data class Modifier(val type: ModifierType, val value: Double)

enum class ModifierCategory {
    ATTACK,
    DAMAGE,
    DEFENSE,
    RESISTANCE,
    CRIT,
    SPEED,
    RESOURCE_POOL,
    RESOURCE_REGEN,
    LUCK,
    CHARACTER
}

enum class ModifierType(val category: ModifierCategory) {
    // Damage
    ALL_DAMAGE(ModifierCategory.DAMAGE),
    SPELL_DAMAGE(ModifierCategory.DAMAGE),
    PHYSICAL_DAMAGE(ModifierCategory.DAMAGE),
    MENTAL_DAMAGE(ModifierCategory.DAMAGE),
    POISON_DAMAGE(ModifierCategory.DAMAGE),
    FIRE_DAMAGE(ModifierCategory.DAMAGE),
    COLD_DAMAGE(ModifierCategory.DAMAGE),
    ENERGY_DAMAGE(ModifierCategory.DAMAGE),
    LIGHT_DAMAGE(ModifierCategory.DAMAGE),
    DARK_DAMAGE(ModifierCategory.DAMAGE),

    // NB: Probably will end up wanting one for each damage type, but let's go with this for now
    INCOMING_DAMAGE_PENETRATION(ModifierCategory.DAMAGE),

    // Damage Reduction
    INCOMING_ALL_DAMAGE(ModifierCategory.RESISTANCE),
    INCOMING_PHYSICAL_DAMAGE(ModifierCategory.RESISTANCE),
    INCOMING_MENTAL_DAMAGE(ModifierCategory.RESISTANCE),
    INCOMING_POISON_DAMAGE(ModifierCategory.RESISTANCE),
    INCOMING_FIRE_DAMAGE(ModifierCategory.RESISTANCE),
    INCOMING_COLD_DAMAGE(ModifierCategory.RESISTANCE),
    INCOMING_ENERGY_DAMAGE(ModifierCategory.RESISTANCE),
    INCOMING_LIGHT_DAMAGE(ModifierCategory.RESISTANCE),
    INCOMING_DARK_DAMAGE(ModifierCategory.RESISTANCE),

    // TODO: Spell/ability effectiveness?

    // Crit
    CRIT_CHANCE(ModifierCategory.CRIT),
    CRIT_DAMAGE(ModifierCategory.CRIT),

    ACTION_COST(ModifierCategory.SPEED),
    DODGE(ModifierCategory.DEFENSE),

    // Resources
    HP_PER_LEVEL(ModifierCategory.RESOURCE_POOL),
    STAMINA_PER_LEVEL(ModifierCategory.RESOURCE_POOL),
    MANA_PER_LEVEL(ModifierCategory.RESOURCE_POOL),
    RAGE_PER_LEVEL(ModifierCategory.RESOURCE_POOL),
    HP_REGEN(ModifierCategory.RESOURCE_REGEN),
    STAMINA_REGEN(ModifierCategory.RESOURCE_REGEN),
    MANA_REGEN(ModifierCategory.RESOURCE_REGEN),
    RAGE_GEN(ModifierCategory.RESOURCE_REGEN),

    ACCURACY(ModifierCategory.ATTACK),
    SKILL_POINTS_PER_LEVEL(ModifierCategory.CHARACTER),
    COOLDOWN(ModifierCategory.SPEED),
    OFF_HAND(ModifierCategory.ATTACK),

    // Luck
    RARITY_FIND(ModifierCategory.LUCK),
    GOLD_DROP(ModifierCategory.LUCK)
}