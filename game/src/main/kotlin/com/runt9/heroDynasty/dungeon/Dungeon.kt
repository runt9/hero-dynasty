package com.runt9.heroDynasty.dungeon

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.utils.viewport.StretchViewport
import com.runt9.heroDynasty.dungeon.input.DungeonMouseInfo
import com.runt9.heroDynasty.lib.AppConst.baseFov
import com.runt9.heroDynasty.lib.AppConst.bigHeight
import com.runt9.heroDynasty.lib.AppConst.bigWidth
import com.runt9.heroDynasty.lib.AppConst.cellHeight
import com.runt9.heroDynasty.lib.AppConst.cellWidth
import com.runt9.heroDynasty.lib.AppConst.viewportHeight
import com.runt9.heroDynasty.lib.AppConst.viewportWidth
import com.runt9.heroDynasty.util.getCoord
import squidpony.squidgrid.Direction
import squidpony.squidgrid.FOV
import squidpony.squidgrid.Radius
import squidpony.squidgrid.gui.gdx.AnimatedEntity
import squidpony.squidgrid.gui.gdx.DefaultResources
import squidpony.squidgrid.gui.gdx.ImageSquidPanel
import squidpony.squidgrid.gui.gdx.SquidLayers
import squidpony.squidgrid.mapping.DungeonUtility
import squidpony.squidmath.Coord
import squidpony.squidmath.GreasedRegion
import squidpony.squidmath.LightRNG
import squidpony.squidmath.RNG

class Dungeon {
    internal val rawDungeon: Array<CharArray>
    private val visibleTiles = Array(bigWidth, { DoubleArray(bigHeight) })
    private val resistances: Array<DoubleArray>
    internal val player: AnimatedEntity
    private val display: ImageSquidPanel
    private val layers: SquidLayers
    private val pendingMoves = ArrayList<Coord>(200)
    private val batch: SpriteBatch

    lateinit var mouseInfo: DungeonMouseInfo
    val stage: Stage

    lateinit var blockage: GreasedRegion
    lateinit var seen: GreasedRegion

    init {
//        val atlas = DefaultResources.getIconAtlas()

        val atlas = TextureAtlas(Gdx.files.internal("dungeon_tiles.atlas"))

        layers = SquidLayers(bigWidth, bigHeight, cellWidth, cellHeight, DefaultResources.getSquareSmoothFont())

        display = ImageSquidPanel(bigWidth, bigHeight, layers.textFactory)
        display.setImageSwap('#', atlas.findRegion("wall"))
        display.setImageSwap('~', atlas.findRegion("water"))
        display.setImageSwap('+', atlas.findRegion("door-closed"))
        display.setImageSwap('/', atlas.findRegion("door-closed"))
        display.setImageSwap('@', atlas.findRegion("dwarf"))
        display.setImageSwap('.', atlas.findRegion("floor"))
        layers.foregroundLayer = display

        rawDungeon = DungeonGenerator().generateDungeon()
        resistances = DungeonUtility.generateResistances(rawDungeon)
        val playerCoord = GreasedRegion(rawDungeon, '.').singleRandom(RNG(LightRNG()))
        player = layers.animateActor(playerCoord.x, playerCoord.y, '@', Color.WHITE)
        layers.addActor(player.actor)

        batch = SpriteBatch()
        stage = Stage(StretchViewport(viewportWidth, viewportHeight), batch)
        stage.addActor(layers)
    }

    fun rebuildFov() {
        FOV.reuseFOV(resistances, visibleTiles, player.gridX, player.gridY, baseFov, Radius.CIRCLE)
        if (!::blockage.isInitialized) {
            blockage = GreasedRegion(visibleTiles, 0.0)
            seen = blockage.not().copy()
        } else {
            blockage.refill(visibleTiles, 0.0)
            seen.or(blockage.not())
        }

        blockage.fringe8way()
        mouseInfo.resetPlayerToCursor(player.getCoord(), blockage)
        layout.updateVision(visibleTiles, seen)
    }

    fun moveCharacter(coord: Coord) = moveCharacter(coord.x, coord.y)

    private fun moveCharacter(xMod: Int, yMod: Int) {
        val newX = player.gridX + xMod
        val newY = player.gridY + yMod

        if (newX >= 0 && newY >= 0 && newX < bigWidth && newY < bigHeight && rawDungeon[newX][newY] != '#') {
            display.slide(player, newX, newY, 0.03f)
            rebuildFov()
        } else {
            display.bump(player, Direction.getRoughDirection(xMod, yMod), 0.1f)
        }
    }

    fun render() {
        stage.camera.position.x = (player.gridX * cellWidth).toFloat()
        stage.camera.position.y = ((bigHeight - player.gridY) * cellHeight).toFloat()

        rawDungeon.forEachIndexed { i, next ->
            next.forEachIndexed loop@{ j, _ ->
                val endColor = when {
                    visibleTiles[i][j] > 0.0 -> Color.WHITE
                    seen.contains(i, j) -> Color.GRAY
                    else -> return@loop
                }

                layers.put(i, j, rawDungeon[i][j], endColor, Color.BLACK, 40)
            }
        }
    }

    fun handleMoves(): Boolean {
        if (pendingMoves.isEmpty() || display.hasActiveAnimations()) {
            return false
        }

        val move = pendingMoves.removeAt(0)

        if (!mouseInfo.toCursor.isEmpty()) {
            mouseInfo.toCursor.removeAt(0)
        }

        moveCharacter(move.x - player.gridX, move.y - player.gridY)
        if (pendingMoves.isEmpty()) {
            mouseInfo.resetPlayerToCursor(player.getCoord(), blockage, true)
        }

        return true
    }

    fun draw() {
        stage.act()
        stage.viewport.apply(false)
        stage.draw()
        batch.begin()
        layers.drawActor(batch, 1f, player)
        batch.end()
    }

    fun hasPendingMoves() = !pendingMoves.isEmpty()
    fun moveToMouse() {
        if (visibleTiles[mouseInfo.cursor.x][mouseInfo.cursor.y] > 0.0) {
            pendingMoves.addAll(mouseInfo.toCursor)
        }
    }
}