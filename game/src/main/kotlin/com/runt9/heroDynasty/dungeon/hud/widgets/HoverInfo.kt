package com.runt9.heroDynasty.dungeon.hud.widgets

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.scenes.scene2d.ui.WidgetGroup
import com.runt9.heroDynasty.character.*
import com.runt9.heroDynasty.core.getPixmapDrawable
import com.runt9.heroDynasty.dungeon.assets.getFont
import com.runt9.heroDynasty.util.humanReadable
import com.runt9.heroDynasty.util.toScale



// TODO: Could probably use some touch ups, like scaling the font down since at high levels there will be too many modifiers to show
class HoverInfo(private val skin: Skin) : WidgetGroup() {
    private val infoPanel = Table()
    private var character: Character? = null

    init {
        infoPanel.width = 256f
        infoPanel.height = 512f
        infoPanel.background = getPixmapDrawable(256, 512, Color(0.5f, 0.5f, 0.5f, 0.1f))
        infoPanel.top().left().pad(5f).defaults().left().padRight(5f)
        addActor(infoPanel)
    }

    fun update(character: Character?) {
        this.character = character

        infoPanel.clear()
        if (character == null) {
            return
        }

        val style = LabelStyle(getFont(14), Color.WHITE)

        infoPanel.add(Label("Character: ", style))
        infoPanel.add(Label(character.name, style))

        if (character is Npc) {
            infoPanel.row()
            infoPanel.add(Label("Enemy:", style))
            infoPanel.add(Label("[#${character.powerLevel.color}]${character.powerLevel.humanReadable()}[]", style))
        }

        infoPanel.row()
        infoPanel.add(Label("Level:", style))
        infoPanel.add(Label(character.level.toString(), style))
        infoPanel.row()
        infoPanel.add(Label("HP:", style))
        infoPanel.add(Label("${character.hitPoints.current.toScale(2)} / ${character.hitPoints.max}", style))

        character.modifiers.categorize(ModifierCategory.ATTACK, ModifierCategory.DAMAGE, ModifierCategory.DEFENSE,
                ModifierCategory.RESISTANCE, ModifierCategory.CRIT, ModifierCategory.SPEED, ModifierCategory.RESOURCE_REGEN
        ).toSortedMap(compareBy { it.name }).forEach { category, modifiers ->
            infoPanel.row()
            infoPanel.add(Label("${category.humanReadable()}:", style))

            modifiers.groupByType().forEach { (type, sum) ->
                infoPanel.row()
                val smallStyle = LabelStyle(getFont(12), Color.WHITE)

                val typeLabel = Label(" - ${type.humanReadable()}:", smallStyle)
                infoPanel.add(typeLabel)
                val modifierLabel = Label("${sum.toScale(2)}x", smallStyle)
                infoPanel.add(modifierLabel)
            }
        }
    }

    override fun draw(batch: Batch, parentAlpha: Float) {
        update(character) // Force update on draw to update things like HP and modifier changes
        super.draw(batch, parentAlpha)
    }
}