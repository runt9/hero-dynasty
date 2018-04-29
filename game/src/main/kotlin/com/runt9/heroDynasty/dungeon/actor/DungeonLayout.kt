package com.runt9.heroDynasty.dungeon.actor

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.Group
import com.badlogic.gdx.scenes.scene2d.actions.Actions
import com.runt9.heroDynasty.character.Character
import com.runt9.heroDynasty.core.rng
import com.runt9.heroDynasty.util.AppConst.cellHeight
import com.runt9.heroDynasty.util.AppConst.cellWidth
import squidpony.squidgrid.Direction
import squidpony.squidmath.Coord
import squidpony.squidmath.GreasedRegion
import kotlin.math.abs

class DungeonLayout(private val dungeon: Array<CharArray>, private val assetMap: Map<Char, List<AtlasRegion>>) : Group() {
    private var animationCount = 0
    private val texturedDungeon: MutableMap<Coord, TextureAtlas.AtlasRegion>
    lateinit var visibleTiles: Array<DoubleArray>
    lateinit var seen: GreasedRegion

     init {
         texturedDungeon = mapDungeonToTextures(dungeon, assetMap)
     }

    // TODO: Race condition causing this to not go back to 0?
    // Appears to be when an enemy doesn't have a direct path and starts moving away. Once they're out of FOV,
    // this doesn't go back to 0.
    fun isAnimating() = animationCount > 0

    fun addCharacter(sprite: TextureRegion, coordinates: Coord, character: Character): CharacterSprite {
        val characterSprite = CharacterSprite(sprite, coordinates.x, coordinates.y, character)
        addActor(characterSprite)
        return characterSprite
    }

    // TODO: These functions are cool and all, but animations slow the game down a lot and complicate the code, are we sure we want them?
    fun slide(character: CharacterSprite, newX: Int, newY: Int, duration: Float, callback: (() -> Unit)? = null) {
        animationCount++
        character.addAction(Actions.sequence(
                Actions.moveTo(newX * cellWidth, newY * cellWidth, duration),
                Actions.delay(duration, Actions.run {
                    --animationCount
                    callback?.invoke()
                })))
    }

    fun bump(character: CharacterSprite, direction: Direction, duration: Float, callback: (() -> Unit)? = null) {
        animationCount++
        character.addAction(Actions.sequence(
                Actions.moveTo(character.x + direction.deltaX.toFloat() * 0.35f * cellWidth,
                        character.y + direction.deltaY.toFloat() * 0.35f * cellHeight, duration * 0.15f),
                Actions.moveTo(character.x, character.y, duration * 0.45f),
                Actions.delay(duration, Actions.run {
                    --animationCount
                    callback?.invoke()
                })))
    }

    override fun draw(batch: Batch, parentAlpha: Float) {
        texturedDungeon.forEach { coord, texture ->
            val x = coord.x
            val y = coord.y

            val tileLight = visibleTiles[x][y].toFloat()

            val endColor = when {
                tileLight > 0.0 -> Color(1f, 1f, 1f, tileLight + 0.1f)
                seen.contains(x, y) -> Color(1f, 1f, 1f, 0.1f)
                else -> return@forEach
            }

            batch.color = endColor
            batch.draw(texture, x.toFloat() * cellWidth, y.toFloat() * cellHeight, cellWidth, cellHeight)
        }

        children.filterIsInstance(CharacterSprite::class.java).forEach {
            val coord = it.coord
            if (visibleTiles[coord.x][coord.y] > 0.0) {
                it.act(Gdx.graphics.deltaTime)
                it.isVisible = true
            } else {
                it.isVisible = false
            }
        }

        super.draw(batch, parentAlpha)
    }

    fun updateVision(visibleTiles: Array<DoubleArray>, seen: GreasedRegion) {
        this.visibleTiles = visibleTiles
        this.seen = seen
    }

    private fun mapDungeonToTextures(dungeon: Array<CharArray>, assetMap: Map<Char, List<AtlasRegion>>): MutableMap<Coord, AtlasRegion> {
        val retVal = mutableMapOf<Coord, TextureAtlas.AtlasRegion>()

        dungeon.forEachIndexed { x, next ->
            next.forEachIndexed { y, tile ->
                val randomTile = rng.shuffle(assetMap[tile])[0]
                retVal[Coord.get(x, y)] = randomTile
            }
        }

        return retVal
    }

    fun openDoor(x: Int, y: Int) {
        dungeon[x][y] = '/'
        texturedDungeon[Coord.get(x, y)] = assetMap['/']!![0]
    }

    fun removeCharacter(sprite: CharacterSprite) = removeActor(sprite)

    fun doFloatingNumber(healthDiff: Int, attacker: CharacterSprite, defender: CharacterSprite, heal: Boolean = false) {
        val floatingNumber = FloatingNumber(healthDiff)
        addActor(floatingNumber)
        val attackVector = Direction.getRoughDirection(defender.coord.x - attacker.coord.x, defender.coord.y - attacker.coord.y)
        floatingNumber.x = defender.x + 10
        floatingNumber.y = defender.y + cellHeight / 2
        floatingNumber.color = if (heal) Color.GREEN else Color.RED
        floatingNumber.addAction(Actions.parallel(
                Actions.moveBy(40f * attackVector.deltaX, 40f * attackVector.deltaY, 2f),
                Actions.fadeOut(2f),
                Actions.scaleTo(1f, 1f, 2f)
        ))
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