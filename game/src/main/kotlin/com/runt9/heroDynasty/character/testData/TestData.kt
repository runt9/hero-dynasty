package com.runt9.heroDynasty.character.testData

import com.runt9.heroDynasty.character.Player
import com.runt9.heroDynasty.character.item.weapon.Weapon
import com.runt9.heroDynasty.character.npc.Npc
import com.runt9.heroDynasty.character.npc.NpcPowerLevel
import com.runt9.heroDynasty.character.race.Race
import com.runt9.heroDynasty.core.rng
import com.runt9.heroDynasty.util.humanReadable
import org.reflections.Reflections

val monsters = listOf(
        "air_elemental",
        "ant",
        "bear",
        "bee",
        "bone_dragon",
        "cyclops",
        "dark_draconian",
        "deathcap",
        "death_knight",
        "dryad",
        "dwarf_berserker",
        "dwarf_fighter",
        "earth_elemental",
        "elf_blademaster",
        "elf_fighter",
        "elf_sorcerer",
        "faun",
        "fire_elemental",
        "fire_giant",
        "formicid",
        "frost_giant",
        "gargoyle",
        "ghost",
        "ghoul",
        "goblin",
        "gold_dragon",
        "green_dragon",
        "griffon",
        "guardian_naga",
        "hell_hound",
        "hell_knight",
        "hobgoblin",
        "human_enchantress",
        "hydra",
        "ice_devil",
        "imp",
        "iron_dragon",
        "juggernaut",
        "kobold",
        "lich",
        "manticore",
        "minotaur",
        "mummy",
        "ogre",
        "orc_knight",
        "orc_sorcerer",
        "profane_servitor",
        "quasit",
        "quicksilver_dragon",
        "rakshasa",
        "rat",
        "satyr",
        "scorpion",
        "shadow",
        "shadow_demon",
        "shadow_dragon",
        "skeleton",
        "snake",
        "soul_eater",
        "stone_giant",
        "storm_dragon",
        "swamp_dragon",
        "tengu_conjurer",
        "tengu_reaver",
        "tengu_warrior",
        "treant",
        "troll",
        "vampire_knight",
        "vampire_mage",
        "vine_stalker",
        "water_elemental",
        "wolf",
        "wraith",
        "wyvern",
        "zombie",
        "zombie_ogre",
        "zombie_rat"
)

val testPlayer = run {
    val player = Player(rng.getRandomElement(Reflections("com.runt9.heroDynasty.character.race").getSubTypesOf(Race::class.java)).newInstance(), "runt9")
    player.inventory.primaryHand = rng.getRandomElement(Reflections("com.runt9.heroDynasty.character.item.weapon").getSubTypesOf(Weapon::class.java)).newInstance()
    player.recalculateModifiers()
    player.applyModifiers()
    return@run player
}

val testEnemies = (0 until 80).map {
    val powerLevel = when {
        it % 20 == 0 -> NpcPowerLevel.BOSS
        it % 5 == 0 -> NpcPowerLevel.GUARD
        it % 2 == 0 -> NpcPowerLevel.MINION
        else -> NpcPowerLevel.CREATURE
    }

    return@map Npc(powerLevel, rng.getRandomElement(monsters).humanReadable())
}