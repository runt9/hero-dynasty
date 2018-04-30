package com.runt9.heroDynasty.dungeon.hud

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.scenes.scene2d.ui.WidgetGroup
import com.runt9.heroDynasty.character.Character
import com.runt9.heroDynasty.character.Npc
import com.runt9.heroDynasty.character.sumAsModifiers
import com.runt9.heroDynasty.core.getPixmapDrawable
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

        infoPanel.add(Label("Character: ", skin))
        infoPanel.add(Label(character.name, skin))

        if (character is Npc) {
            infoPanel.row()
            infoPanel.add(Label("Enemy:", skin))
            infoPanel.add(Label("[#${character.powerLevel.color}]${character.powerLevel.humanReadable()}[]", skin))
        }

        infoPanel.row()
        infoPanel.add(Label("Level:", skin))
        infoPanel.add(Label(character.level.toString(), skin))
        infoPanel.row()
        infoPanel.add(Label("HP:", skin))
        infoPanel.add(Label("${character.hitPoints.current.toScale(2)} / ${character.hitPoints.max}", skin))

        character.modifiers.forEach { type, modifiers ->
            infoPanel.row()
            infoPanel.add(Label("${type.humanReadable()}:", skin))
            infoPanel.add(Label("${modifiers.sumAsModifiers().toScale(2)}x", skin))
        }
    }

    override fun draw(batch: Batch, parentAlpha: Float) {
        update(character) // Force update on draw to update things like HP and modifier changes
        super.draw(batch, parentAlpha)
    }
}