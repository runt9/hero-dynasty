package com.runt9.heroDynasty.dungeon.actor

import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.scenes.scene2d.Actor
import com.runt9.heroDynasty.lib.AppConst.cellHeight
import com.runt9.heroDynasty.lib.AppConst.cellWidth
import squidpony.squidmath.Coord

class Character(private val sprite: TextureRegion, x: Int, y: Int) : Actor() {
    val gridX: Int get() = (x / cellWidth).toInt()
    val gridY: Int get() = (y / cellHeight).toInt()

    init {
        this.x = x.toFloat() * cellWidth
        this.y = y.toFloat() * cellHeight
    }

    fun getCoord(): Coord = Coord.get(gridX, gridY)

    override fun draw(batch: Batch, parentAlpha: Float) {
        super.draw(batch, parentAlpha)
        batch.draw(sprite, x, y, cellWidth, cellHeight)
    }
}