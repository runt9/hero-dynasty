package com.runt9.heroDynasty.character

fun MutableList<Double>.sumAsModifiers() = this.sum() - (this.size - 1)

fun MutableMap<ModifierType, MutableList<Double>>.addModifier(type: ModifierType, value: Double) {
    if (this.containsKey(type)) {
        this[type]?.add(value)
    } else {
        this[type] = mutableListOf(value)
    }
}

enum class ModifierType {
    // Damage
    ALL_DAMAGE,
    SPELL_DAMAGE,
    PHYSICAL_DAMAGE,
    MENTAL_DAMAGE,
    POISON_DAMAGE,
    FIRE_DAMAGE,
    COLD_DAMAGE,
    ENERGY_DAMAGE,
    LIGHT_DAMAGE,
    DARK_DAMAGE,

    // NB: Probably will end up wanting one for each damage type, but let's go with this for now
    INCOMING_DAMAGE_PENETRATION,

    // Damage Reduction
    INCOMING_ALL_DAMAGE,
    INCOMING_PHYSICAL_DAMAGE,
    INCOMING_MENTAL_DAMAGE,
    INCOMING_POISON_DAMAGE,
    INCOMING_FIRE_DAMAGE,
    INCOMING_COLD_DAMAGE,
    INCOMING_ENERGY_DAMAGE,
    INCOMING_LIGHT_DAMAGE,
    INCOMING_DARK_DAMAGE,

    // TODO: Spell/ability effectiveness?

    // Crit
    CRIT_CHANCE,
    CRIT_DAMAGE,

    ACTION_COST,
    DODGE,

    // Resources
    HP_PER_LEVEL,
    STAMINA_PER_LEVEL,
    MANA_PER_LEVEL,
    RAGE_PER_LEVEL,
    HP_REGEN,
    STAMINA_REGEN,
    MANA_REGEN,
    RAGE_GEN,

    ACCURACY,
    SKILL_POINTS_PER_LEVEL,
    COOLDOWN,
    OFF_HAND,

    MORALITY,

    // Luck
    AUTO_SUCCEED,
    RARITY_FIND,
    GOLD_DROP
    // TODO: World map stuff
}