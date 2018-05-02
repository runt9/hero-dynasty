package com.runt9.heroDynasty.dungeon.hud.menu

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.*
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener
import com.runt9.heroDynasty.character.Player
import com.runt9.heroDynasty.character.categorize
import com.runt9.heroDynasty.character.groupByType
import com.runt9.heroDynasty.dungeon.assets.getFont
import com.runt9.heroDynasty.util.humanReadable
import com.runt9.heroDynasty.util.toScale

class CharacterSheet(skin: Skin, private val player: Player) : AbstractMenu("${player.name} Character Sheet", skin) {
    // TODO: Show diff from last time character sheet was opened
    // TODO: Show tooltip on modifier hover to show sources
    // TODO: Better organization with headings
    // TODO: Borders
    // TODO: Needs to look less terrible
    // TODO: Adding/subtracting attributes should only temporarily modify modifiers, and highlight the changed ones
    // TODO: Maybe need to refactor into multiple classes

    private var spentAttributes = 0

    init {
        pad(20f)
        contentTable.defaults().top()
    }

    fun update() {
        contentTable.clear()
        contentTable.add(getInfoTable())
        contentTable.add(getAttributeTable())
        contentTable.add(getWeaponsTable()).colspan(2)
        contentTable.row()
        contentTable.add(getModifiersTable()).colspan(4)
    }

    override fun show(stage: Stage?): Dialog {
        update()
        return super.show(stage)
    }

    private fun getInfoTable(): Table {
        val infoTable = Table(skin)
        infoTable.padRight(20f).defaults().left().padRight(10f)
        infoTable.add("Character Information:")
        infoTable.row()
        infoTable.add("Race:")
        infoTable.add(player.race::class.simpleName?.humanReadable())
        infoTable.row()
        infoTable.add("Level (XP):")
        infoTable.add("${player.level} (${player.xp} / ${player.xpToNextLevel})")
        infoTable.row()
        return infoTable
    }

    private fun getAttributeTable(): Table {
        val attributeTable = Table(skin)
        attributeTable.padRight(20f).defaults().left().padRight(10f)
        attributeTable.add("Attributes:")
        attributeTable.row()
        attributeTable.add("Available Points:")
        attributeTable.add(player.attributePoints.toString())
        attributeTable.row()
        player.attributes.forEach {
            attributeTable.add("${it::class.simpleName}:")
            if (spentAttributes > 0) {
                val minusButton = TextButton("-", skin)
                minusButton.addListener(object : ClickListener() {
                    override fun clicked(event: InputEvent?, x: Float, y: Float) {
                        it.value--
                        spentAttributes--
                        player.attributePoints++
                        player.recalculateModifiers()
                        update()
                    }
                })
                attributeTable.add(minusButton)
            }
            attributeTable.add(it.toString())
            if (player.attributePoints > 0) {
                val addButton = TextButton("+", skin)
                addButton.addListener(object : ClickListener() {
                    override fun clicked(event: InputEvent?, x: Float, y: Float) {
                        it.value++
                        spentAttributes++
                        player.attributePoints--
                        player.recalculateModifiers()
                        update()
                    }
                })
                attributeTable.add(addButton)
            }
            attributeTable.row()
        }
        if (spentAttributes > 0) {
            val saveButton = TextButton("Save", skin)
            saveButton.addListener(object : ClickListener() {
                override fun clicked(event: InputEvent?, x: Float, y: Float) {
                    spentAttributes = 0
                    update()
                }
            })
            attributeTable.add(saveButton)
        }
        return attributeTable
    }

    private fun getWeaponsTable(): Table {
        val weaponsTable = Table(skin)
        weaponsTable.defaults().left()
        weaponsTable.add("Weapon Information:")
        weaponsTable.row()
        
        val primaryHandTable = Table(skin)
        primaryHandTable.padRight(20f).defaults().left().padRight(10f)
        primaryHandTable.add("Primary Hand:")
        if (player.inventory.primaryHand == null) {
            // TODO: Show unarmed stats
            primaryHandTable.add("None")
            primaryHandTable.row()
        } else {
            val weapon = player.inventory.primaryHand!!
            primaryHandTable.add(weapon.getName())
            primaryHandTable.row()
            primaryHandTable.add(" - Range:")
            primaryHandTable.add(weapon.range.toString())
            primaryHandTable.row()
            primaryHandTable.add(" - Damage:")
            primaryHandTable.add("${weapon.damageRange.first.toScale(2)}x - ${weapon.damageRange.second.toScale(2)}x")
            primaryHandTable.row()
            weapon.modifiers.sortedBy { it.type.name }.forEach {
                primaryHandTable.add(" - ${it.type.displayName}: ")
                primaryHandTable.add("${it.value.toScale(2)}x")
                primaryHandTable.row()
            }
        }

        val offHandTable = Table(skin)
        offHandTable.padRight(20f).defaults().left().padRight(10f)
        offHandTable.add("Off Hand:")
        if (player.inventory.offHand == null) {
            offHandTable.add("None")
        } else {
            // TODO: Off-hand penalty
            val weapon = player.inventory.offHand!!
            offHandTable.add(weapon.getName())
            offHandTable.row()
            offHandTable.add(" - Range:")
            offHandTable.add(weapon.range.toString())
            offHandTable.row()
            offHandTable.add(" - Damage Range: ")
            offHandTable.add("${weapon.damageRange.first.toScale(2)}x - ${weapon.damageRange.second.toScale(2)}x")
            offHandTable.row()
            weapon.modifiers.sortedBy { it.type.name }.forEach {
                offHandTable.add(" - ${it.type.displayName}:")
                offHandTable.add("${it.value.toScale(2)}x")
                offHandTable.row()
            }
        }

        weaponsTable.add(primaryHandTable).padRight(20f)
        weaponsTable.add(offHandTable)
        return weaponsTable
    }

    private fun getModifiersTable(): Table {
        val modifiersTable = Table(skin)
        modifiersTable.defaults().top().left().padRight(10f)
        modifiersTable.add("Modifiers:")
        modifiersTable.row()
        player.modifiers.categorize().toSortedMap(compareBy { it.name }).forEach { category, modifiers ->
            val individualModifierTable = Table(skin)
            individualModifierTable.defaults().left()
            individualModifierTable.add("${category.name.humanReadable()}:")

            modifiers.groupByType().forEach typeLoop@{ (type, sum) ->
                if (sum == 1.0) return@typeLoop

                individualModifierTable.row()
                val smallStyle = Label.LabelStyle(getFont(12), Color.WHITE)

                individualModifierTable.add(Label(" - ${type.displayName}:", smallStyle)).padRight(5f)
                individualModifierTable.add(Label("${sum.toScale(2)}x", smallStyle))
            }

            modifiersTable.add(individualModifierTable)
        }
        return modifiersTable
    }
}