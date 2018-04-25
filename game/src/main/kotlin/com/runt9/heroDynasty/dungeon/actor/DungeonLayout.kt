package com.runt9.heroDynasty.dungeon.actor

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.actions.Actions
import com.runt9.heroDynasty.lib.AppConst.cellHeight
import com.runt9.heroDynasty.lib.AppConst.cellWidth
import squidpony.squidgrid.Direction
import squidpony.squidmath.Coord
import squidpony.squidmath.GreasedRegion

class DungeonLayout(private val dungeon: Array<CharArray>, private val tileMap: Map<Char, TextureRegion>) : Actor() {
    private val characters: MutableList<Character> = mutableListOf()
    private var animationCount = 0
    lateinit var visibleTiles: Array<DoubleArray>
    lateinit var seen: GreasedRegion

    fun isAnimating() = animationCount > 0

    fun addCharacter(sprite: TextureRegion, coordinates: Coord): Character {
        val character = Character(sprite, coordinates.x, coordinates.y)
        characters.add(character)
        return character
    }

    fun slide(character: Character, newX: Int, newY: Int, duration: Float, callback: () -> Unit) {
        animationCount++
        character.addAction(Actions.sequence(
                Actions.moveTo(newX * cellWidth, newY * cellWidth, duration),
                Actions.delay(duration, Actions.run {
                    callback()
                    --animationCount
                })))
    }

    fun bump(character: Character, direction: Direction, duration: Float) {
        animationCount++
        character.addAction(Actions.sequence(
                Actions.moveTo(character.x + direction.deltaX.toFloat() * 0.35f * cellWidth,
                        character.y - direction.deltaY.toFloat() * 0.35f * cellHeight, duration * 0.35f),
                Actions.moveTo(character.x, character.y, duration * 0.65f),
                Actions.delay(duration, Actions.run { --animationCount })))
    }

    override fun draw(batch: Batch, parentAlpha: Float) {
        super.draw(batch, parentAlpha)

        dungeon.forEachIndexed { x, next ->
            next.forEachIndexed loop@{ y, tile ->
                val tileLight = visibleTiles[x][y].toFloat()

                val endColor = when {
                    tileLight > 0.0 -> Color(1f, 1f, 1f, tileLight)
                    seen.contains(x, y) -> Color(1f, 1f, 1f, 0.05f)
                    else -> return@loop
                }

                batch.color = endColor
                batch.draw(tileMap[tile], x.toFloat() * cellWidth, y.toFloat() * cellHeight, cellWidth, cellHeight)
            }
        }

        characters.forEach {
            it.act(Gdx.graphics.deltaTime)
            batch.color = Color.WHITE
            it.draw(batch, parentAlpha)
        }
    }

    fun updateVision(visibleTiles: Array<DoubleArray>, seen: GreasedRegion) {
        this.visibleTiles = visibleTiles
        this.seen = seen
    }
}