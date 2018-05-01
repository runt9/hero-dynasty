package com.runt9.heroDynasty.dungeon.actor

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar.ProgressBarStyle
import com.runt9.heroDynasty.character.Character
import com.runt9.heroDynasty.character.item.Item
import com.runt9.heroDynasty.character.npc.Npc
import com.runt9.heroDynasty.core.getPixmapDrawable
import com.runt9.heroDynasty.dungeon.assets.AssetMapper
import com.runt9.heroDynasty.util.AppConst.cellHeight
import com.runt9.heroDynasty.util.AppConst.cellWidth
import com.runt9.heroDynasty.util.toAssetName
import squidpony.squidmath.Coord

class CharacterSprite(private val sprite: TextureRegion, x: Int, y: Int, val character: Character, val assetMapper: AssetMapper) : Actor() {
    val gridX: Int get() = (x / cellWidth).toInt()
    val gridY: Int get() = (y / cellHeight).toInt()
    val coord: Coord get() = Coord.get(gridX, gridY)
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
        progressBarStyle.knobBefore = getPixmapDrawable(3, cellHeight.toInt(), (character as? Npc)?.powerLevel?.color ?: Color.GREEN)

        val healthBar = ProgressBar(0.0f, character.hitPoints.max.toFloat(), 0.01f, true, progressBarStyle)
        healthBar.value = character.hitPoints.current.toFloat()
        healthBar.setBounds(x, y + 4, 3f, cellHeight - 8f)
        return healthBar
    }

    override fun draw(batch: Batch, parentAlpha: Float) {
        batch.color = Color.WHITE
        batch.draw(sprite, x, y, cellWidth, cellHeight)

        // TODO: Make own actor?
        healthBar.value = character.hitPoints.current.toFloat()
        healthBar.setPosition(x, y)
        healthBar.draw(batch, parentAlpha)

        drawItems(batch)

        super.draw(batch, parentAlpha)
    }

    private fun drawItem(batch: Batch, item: Item, suffix: String? = null) {
        var assetName = item::class.simpleName!!.toAssetName()
        if (suffix != null) {
            assetName += "_$suffix"
        }

        batch.draw(assetMapper.getRegion(assetName), x, y, cellWidth, cellHeight)
    }

    private fun drawItems(batch: Batch) {
        val inventory = character.inventory
        if (inventory.primaryHand != null) drawItem(batch, inventory.primaryHand!!, "primary")
        if (inventory.offHand != null) drawItem(batch, inventory.offHand!!, "off")
        if (inventory.head != null) drawItem(batch, inventory.head!!, "head")
        if (inventory.body != null) drawItem(batch, inventory.body!!, "body")
        if (inventory.arms != null) drawItem(batch, inventory.body!!, "arms")
        if (inventory.legs != null) drawItem(batch, inventory.body!!, "legs")
        if (inventory.feet != null) drawItem(batch, inventory.body!!, "feet")
    }
}