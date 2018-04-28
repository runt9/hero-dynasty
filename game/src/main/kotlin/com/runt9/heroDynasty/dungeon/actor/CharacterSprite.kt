package com.runt9.heroDynasty.dungeon.actor

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.Pixmap
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar.ProgressBarStyle
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable
import com.runt9.heroDynasty.character.Character
import com.runt9.heroDynasty.util.AppConst.cellHeight
import com.runt9.heroDynasty.util.AppConst.cellWidth
import squidpony.squidmath.Coord









class CharacterSprite(private val sprite: TextureRegion, x: Int, y: Int, val character: Character) : Actor() {
    val gridX: Int get() = (x / cellWidth).toInt()
    val gridY: Int get() = (y / cellHeight).toInt()
    private val healthBar: ProgressBar

    init {
        this.x = x.toFloat() * cellWidth
        this.y = y.toFloat() * cellHeight
        healthBar = drawHealthBar()
    }

    // TODO: Refactor
    private fun drawHealthBar(): ProgressBar {
        val backgroundPixmap = Pixmap(3, 32, Pixmap.Format.RGBA8888)
        backgroundPixmap.setColor(Color.RED)
        backgroundPixmap.fill()
        val backgroundDrawable = TextureRegionDrawable(TextureRegion(Texture(backgroundPixmap)))

        val knobPixmap = Pixmap(3, 0, Pixmap.Format.RGBA8888)
        knobPixmap.setColor(Color.GREEN)
        knobPixmap.fill()
        val knobDrawable = TextureRegionDrawable(TextureRegion(Texture(knobPixmap)))

        val beforeKnobPixmap = Pixmap(3, 32, Pixmap.Format.RGBA8888)
        beforeKnobPixmap.setColor(Color.GREEN)
        beforeKnobPixmap.fill()
        val beforeKnobDrawable = TextureRegionDrawable(TextureRegion(Texture(beforeKnobPixmap)))

        val progressBarStyle = ProgressBarStyle()
        progressBarStyle.background = backgroundDrawable
        progressBarStyle.knob = knobDrawable
        progressBarStyle.knobBefore = beforeKnobDrawable

        val healthBar = ProgressBar(0.0f, character.hitPoints.max.toFloat(), 0.01f, true, progressBarStyle)
        healthBar.value = character.hitPoints.current.toFloat()
        healthBar.setBounds(x, y + 1, 3f, 30f)
        return healthBar
    }

    fun getCoord(): Coord = Coord.get(gridX, gridY)

    override fun draw(batch: Batch, parentAlpha: Float) {
        super.draw(batch, parentAlpha)
        batch.draw(sprite, x, y, cellWidth, cellHeight)

        healthBar.value = character.hitPoints.current.toFloat()
        healthBar.setPosition(x, y)
        healthBar.draw(batch, parentAlpha)
    }
}