package com.runt9.heroDynasty.dungeon

import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.utils.viewport.StretchViewport
import com.runt9.heroDynasty.dungeon.actor.Character
import com.runt9.heroDynasty.dungeon.actor.DungeonLayout
import com.runt9.heroDynasty.dungeon.assets.AssetMapper
import com.runt9.heroDynasty.dungeon.input.DungeonMouseInfo
import com.runt9.heroDynasty.lib.AppConst.baseFov
import com.runt9.heroDynasty.lib.AppConst.bigHeight
import com.runt9.heroDynasty.lib.AppConst.bigWidth
import com.runt9.heroDynasty.lib.AppConst.viewportHeight
import com.runt9.heroDynasty.lib.AppConst.viewportWidth
import squidpony.squidgrid.Direction
import squidpony.squidgrid.FOV
import squidpony.squidgrid.Radius
import squidpony.squidgrid.mapping.DungeonUtility
import squidpony.squidmath.Coord
import squidpony.squidmath.GreasedRegion
import squidpony.squidmath.LightRNG
import squidpony.squidmath.RNG

class Dungeon {
    internal val rawDungeon: Array<CharArray>
    private var visibleTiles = Array(bigWidth.toInt(), { DoubleArray(bigHeight.toInt()) })
    internal val player: Character
    private val layout: DungeonLayout
    private val pendingMoves = ArrayList<Coord>(200)
    private val batch: SpriteBatch
    private val fov: FOV

    lateinit var mouseInfo: DungeonMouseInfo
    val stage: Stage

    private lateinit var blockage: GreasedRegion
    private lateinit var seen: GreasedRegion

    init {
        val assetMapper = AssetMapper()

        rawDungeon = DungeonGenerator().generateDungeon()
        layout = DungeonLayout(rawDungeon, assetMapper.getAssetMap())
        val playerCoord = GreasedRegion(rawDungeon, '.').singleRandom(RNG(LightRNG()))
        player = layout.addCharacter(assetMapper.getCharacter(), playerCoord)

        batch = SpriteBatch()
        stage = Stage(StretchViewport(viewportWidth, viewportHeight), batch)
        stage.addActor(layout)
        fov = FOV(FOV.RIPPLE)
    }

    fun rebuildFov() {
        visibleTiles = fov.calculateFOV(DungeonUtility.generateResistances(rawDungeon), player.gridX, player.gridY, baseFov, Radius.CIRCLE)
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

    fun queueNewMove(coord: Coord) = pendingMoves.add(Coord.get(player.gridX + coord.x, player.gridY + coord.y))

    private fun moveCharacter(xMod: Int, yMod: Int) {
        val newX = player.gridX + xMod
        val newY = player.gridY + yMod

        // Probably a better place for this stuff
        if (rawDungeon[newX][newY] == '+') {
            layout.openDoor(newX, newY)
            layout.bump(player, Direction.getRoughDirection(xMod, yMod), 0f) { rebuildFov() }
            return
        }

        if (newX >= 0 && newY >= 0 && newX < bigWidth && newY < bigHeight && rawDungeon[newX][newY] != '#') {
            layout.slide(player, newX, newY, 0.03f) { rebuildFov() }
        } else {
            layout.bump(player, Direction.getRoughDirection(xMod, yMod), 0.1f) { rebuildFov() }
        }
    }

    fun handleMoves(): Boolean {
        if (pendingMoves.isEmpty() || layout.isAnimating()) {
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
        stage.camera.position.x = player.x
        stage.camera.position.y = player.y
        layout.clear()
        stage.act()
        stage.viewport.apply(false)
        stage.draw()
    }

    fun hasPendingMoves() = !pendingMoves.isEmpty()
    fun moveToMouse() {
        if (visibleTiles[mouseInfo.cursor.x][mouseInfo.cursor.y] > 0.0) {
            pendingMoves.addAll(mouseInfo.toCursor)
        }
    }
}
