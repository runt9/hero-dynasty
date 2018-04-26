package com.runt9.heroDynasty.dungeon.actor

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.actions.Actions
import com.runt9.heroDynasty.lib.AppConst.cellHeight
import com.runt9.heroDynasty.lib.AppConst.cellWidth
import squidpony.squidgrid.Direction
import squidpony.squidmath.Coord
import squidpony.squidmath.GreasedRegion
import squidpony.squidmath.LightRNG
import squidpony.squidmath.RNG

class DungeonLayout(private val dungeon: Array<CharArray>, private val assetMap: Map<Char, List<AtlasRegion>>) : Actor() {
    private val characters: MutableList<Character> = mutableListOf()
    private var animationCount = 0
    private val texturedDungeon: MutableMap<Coord, TextureAtlas.AtlasRegion>
    lateinit var visibleTiles: Array<DoubleArray>
    lateinit var seen: GreasedRegion

     init {
         texturedDungeon = mapDungeonToTextures(dungeon, assetMap)
     }

    // TODO: Race condition causing this to not go back to 0?
    fun isAnimating() = animationCount > 0

    fun addCharacter(sprite: TextureRegion, coordinates: Coord): Character {
        val character = Character(sprite, coordinates.x, coordinates.y)
        characters.add(character)
        return character
    }

    fun slide(character: Character, newX: Int, newY: Int, duration: Float, callback: (() -> Unit)? = null) {
        animationCount++
        character.addAction(Actions.sequence(
                Actions.moveTo(newX * cellWidth, newY * cellWidth, duration),
                Actions.delay(duration, Actions.run {
                    callback?.invoke()
                    --animationCount
                })))
    }

    fun bump(character: Character, direction: Direction, duration: Float, callback: (() -> Unit)? = null) {
        animationCount++
        character.addAction(Actions.sequence(
                Actions.moveTo(character.x + direction.deltaX.toFloat() * 0.35f * cellWidth,
                        character.y + direction.deltaY.toFloat() * 0.35f * cellHeight, duration * 0.35f),
                Actions.moveTo(character.x, character.y, duration * 0.65f),
                Actions.delay(duration, Actions.run {
                    callback?.invoke()
                    --animationCount
                })))
    }

    override fun draw(batch: Batch, parentAlpha: Float) {
        super.draw(batch, parentAlpha)

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

        characters.forEach {
            val coord = it.getCoord()
            if (visibleTiles[coord.x][coord.y] > 0.0) {
                it.act(Gdx.graphics.deltaTime)
                batch.color = Color.WHITE
                it.draw(batch, parentAlpha)
            }
        }
    }

    fun updateVision(visibleTiles: Array<DoubleArray>, seen: GreasedRegion) {
        this.visibleTiles = visibleTiles
        this.seen = seen
    }

    private fun mapDungeonToTextures(dungeon: Array<CharArray>, assetMap: Map<Char, List<AtlasRegion>>): MutableMap<Coord, AtlasRegion> {
        val retVal = mutableMapOf<Coord, TextureAtlas.AtlasRegion>()
        val rng = RNG(LightRNG())

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
}