package com.runt9.heroDynasty.dungeon.hud.widgets

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.scenes.scene2d.ui.WidgetGroup
import com.runt9.heroDynasty.character.Player
import com.runt9.heroDynasty.core.getPixmapDrawable
import com.runt9.heroDynasty.util.toScale

class InfoPanel(private val skin: Skin, private val player: Player) : WidgetGroup() {
    private val infoPanel = Table()

    init {
        infoPanel.width = 150f
        infoPanel.height = 128f
        infoPanel.background = getPixmapDrawable(150, 128, Color(0.5f, 0.5f, 0.5f, 0.1f))
        addActor(infoPanel)
    }

    fun update() {
        infoPanel.reset()
        infoPanel.add(Label("Player Info", skin)).colspan(2)
        infoPanel.row()
        infoPanel.add(Label("Level:", skin))
        infoPanel.add(Label(player.level.toString(), skin))
        infoPanel.row()
        infoPanel.add(Label("XP:", skin))
        infoPanel.add(Label("${player.xp} / ${player.xpToNextLevel}", skin))
        infoPanel.row()
        infoPanel.add(Label("Gold:", skin))
        infoPanel.add(Label(player.inventory.gold.toScale(2).toString(), skin))
    }

    override fun draw(batch: Batch, parentAlpha: Float) {
        update()
        super.draw(batch, parentAlpha)
    }
}