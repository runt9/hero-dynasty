package com.runt9.heroDynasty.dungeon.actor

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.Group
import com.badlogic.gdx.scenes.scene2d.actions.Actions
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar.ProgressBarStyle
import com.runt9.heroDynasty.character.Character
import com.runt9.heroDynasty.core.getPixmapDrawable
import com.runt9.heroDynasty.util.AppConst.cellHeight
import com.runt9.heroDynasty.util.AppConst.cellWidth
import squidpony.squidmath.Coord
import kotlin.math.abs

class CharacterSprite(private val sprite: TextureRegion, x: Int, y: Int, val character: Character) : Group() {
    val gridX: Int get() = (x / cellWidth).toInt()
    val gridY: Int get() = (y / cellHeight).toInt()
    private val healthBar: ProgressBar

    init {
        this.x = x.toFloat() * cellWidth
        this.y = y.toFloat() * cellHeight
        healthBar = buildHealthBar()
    }

    private fun buildHealthBar(): ProgressBar {
        val progressBarStyle = ProgressBarStyle()
        progressBarStyle.background = getPixmapDrawable(3, cellHeight.toInt(), Color.RED)
        progressBarStyle.knob = getPixmapDrawable(3, 0, Color.GREEN)
        progressBarStyle.knobBefore = getPixmapDrawable(3, cellHeight.toInt(), Color.GREEN)

        val healthBar = ProgressBar(0.0f, character.hitPoints.max.toFloat(), 0.01f, true, progressBarStyle)
        healthBar.value = character.hitPoints.current.toFloat()
        healthBar.setBounds(x, y + 2, 3f, cellHeight - 4f)
        return healthBar
    }

    fun getCoord(): Coord = Coord.get(gridX, gridY)

    override fun draw(batch: Batch, parentAlpha: Float) {
        batch.draw(sprite, x, y, cellWidth, cellHeight)

        // TODO: Make own actor?
        healthBar.value = character.hitPoints.current.toFloat()
        healthBar.setPosition(x, y)
        healthBar.draw(batch, parentAlpha)

        super.draw(batch, parentAlpha)
    }

    fun doFloatingNumber(healthDiff: Int, heal: Boolean = false) {
        val floatingNumber = FloatingNumber(healthDiff)
        floatingNumber.color = if (heal) Color.GREEN else Color.RED
        // TODO: Properly center text
        // TODO: Needs a refactor since this can end up drawn underneath other sprites.
        floatingNumber.x = 6f
        floatingNumber.y = cellHeight + 5
        addActor(floatingNumber)
        floatingNumber.addAction(Actions.parallel(Actions.moveBy(0f, cellHeight, 2f), Actions.fadeOut(2f)))
        floatingNumber.addAction(Actions.after(Actions.removeActor()))
    }

    private class FloatingNumber(private val healthDiff: Int) : Actor() {
        val font = BitmapFont()

        override fun draw(batch: Batch, parentAlpha: Float) {
            font.color = color
            font.draw(batch, if (healthDiff > 0) abs(healthDiff).toString() else "+$healthDiff", x, y)
        }
    }
}