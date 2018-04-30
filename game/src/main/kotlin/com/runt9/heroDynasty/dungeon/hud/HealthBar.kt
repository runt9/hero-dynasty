package com.runt9.heroDynasty.dungeon.hud

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.GlyphLayout
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar
import com.badlogic.gdx.scenes.scene2d.ui.Widget
import com.badlogic.gdx.scenes.scene2d.ui.WidgetGroup
import com.runt9.heroDynasty.character.Player
import com.runt9.heroDynasty.core.getPixmapDrawable
import com.runt9.heroDynasty.util.toScale

class HealthBar(private val player: Player) : WidgetGroup() {
    private val healthBar: ProgressBar
    private val healthBarText: HealthBarText = HealthBarText(player)

    init {
        val progressBarStyle = ProgressBar.ProgressBarStyle()
        progressBarStyle.background = getPixmapDrawable(12 * 64, 16, Color.RED)
        progressBarStyle.knob = getPixmapDrawable(0, 16, Color.GREEN)
        progressBarStyle.knobBefore = getPixmapDrawable(12 * 64, 16, Color.GREEN)

        healthBar = ProgressBar(0.0f, player.hitPoints.max.toFloat(), 0.01f, false, progressBarStyle)
        healthBar.value = player.hitPoints.current.toFloat()
        healthBar.setBounds(0f, 0f, 12 * 64f, 16f)
        addActor(healthBar)
        addActor(healthBarText)
    }

    override fun draw(batch: Batch, parentAlpha: Float) {
        healthBar.value = player.hitPoints.current.toFloat()
        super.draw(batch, parentAlpha)
    }

    class HealthBarText(private val player: Player) : Widget() {
        private val font = BitmapFont()
        private val glyphLayout = GlyphLayout()

        override fun draw(batch: Batch, parentAlpha: Float) {
            super.draw(batch, parentAlpha)

            font.color = Color.WHITE
            val text = "${player.hitPoints.current.toInt()} / ${player.hitPoints.max.toInt()} (${player.hitPoints.regen.toScale(2)})"
            glyphLayout.setText(font, text)
            x = 64f * 6 - glyphLayout.width / 2
            y = glyphLayout.height + 3f
            font.draw(batch, glyphLayout, x, y)
        }
    }
}