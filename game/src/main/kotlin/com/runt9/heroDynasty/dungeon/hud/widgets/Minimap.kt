package com.runt9.heroDynasty.dungeon.hud.widgets

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.scenes.scene2d.ui.Widget
import com.runt9.heroDynasty.core.getPixmapDrawable
import com.runt9.heroDynasty.dungeon.actor.CharacterSprite
import squidpony.squidmath.Coord
import squidpony.squidmath.GreasedRegion
import squidpony.squidmath.OrderedMap

class Minimap(private val dungeon: Array<CharArray>, private val player: CharacterSprite, private val enemies: OrderedMap<Coord, CharacterSprite>) : Widget() {
    private val font = BitmapFont()
    private lateinit var seen: GreasedRegion
    private lateinit var visible: Array<DoubleArray>
    private val background = getPixmapDrawable(256, 256, Color(0.8f, 0.8f, 0.8f, 0.5f))

    override fun draw(batch: Batch, parentAlpha: Float) {
        batch.draw(background.region, x, y)

        font.color = Color.BLACK
        font.data.scaleX = 0.4f
        font.data.scaleY = 0.4f

        dungeon.forEachIndexed { x, rows ->
            rows.forEachIndexed { y, cell ->
                if (seen.contains(x, y)) {
                    val char = if (cell == '.') ' ' else cell
                    font.draw(batch, char.toString(), this.x + x * 3f, this.y + y * 3f)
                }
            }
        }

        font.data.scaleX = 1.25f
        font.data.scaleY = 1.25f

        font.color = Color.GREEN
        // TODO: Magic 5 to account for font size
        font.draw(batch, "*", x + player.coord.x * 3f, y + (player.coord.y + 1) * 3f)

        font.color = Color.RED
        enemies.forEach { coord, _ ->
            if (visible[coord.x][coord.y] > 0.0) {
                font.draw(batch, "*", x + coord.x * 3f, y + (coord.y + 1) * 3f)
            }
        }
    }

    fun update(visible: Array<DoubleArray>, seen: GreasedRegion) {
        this.visible = visible
        this.seen = seen
    }
}