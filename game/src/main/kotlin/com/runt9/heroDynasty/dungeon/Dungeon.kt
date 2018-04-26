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
import squidpony.squidai.DijkstraMap
import squidpony.squidgrid.Direction
import squidpony.squidgrid.FOV
import squidpony.squidgrid.Radius
import squidpony.squidgrid.mapping.DungeonUtility
import squidpony.squidmath.*

class Dungeon {
    enum class Phase { WAIT, PLAYER_ANIM, MONSTER_ANIM }
    var phase = Phase.WAIT

    val stage: Stage
    private val layout: DungeonLayout
    internal val rawDungeon: Array<CharArray>

    internal val player: Character
    private val monsters: OrderedMap<Coord, Character> = OrderedMap(20)
    private val fov: FOV

    private val pendingMoves = ArrayList<Coord>(200)
    lateinit var mouseInfo: DungeonMouseInfo

    private var visibleTiles = Array(bigWidth.toInt(), { DoubleArray(bigHeight.toInt()) })
    private lateinit var blockage: GreasedRegion
    private lateinit var seen: GreasedRegion

    private val getToPlayer: DijkstraMap
    private val rng = RNG(LightRNG())

    init {
        val assetMapper = AssetMapper()

        rawDungeon = DungeonGenerator().generateDungeon()
        layout = DungeonLayout(rawDungeon, assetMapper.getAssetMap())
        val floors = GreasedRegion(rawDungeon, '.')
        val playerCoord = floors.singleRandom(rng)
        player = layout.addCharacter(assetMapper.getCharacter(), playerCoord)
        floors.remove(element = playerCoord)
        (0 until 20).forEach {
            val monsterCoord = floors.singleRandom(rng)
            floors.remove(element = monsterCoord)
            monsters[monsterCoord] = layout.addCharacter(assetMapper.getMonster(), monsterCoord)
        }
        getToPlayer = DijkstraMap(rawDungeon, DijkstraMap.Measurement.EUCLIDEAN)

        stage = Stage(StretchViewport(viewportWidth, viewportHeight), SpriteBatch())
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
        } else if (monsters.containsKey(Coord.get(newX, newY))) {
            layout.bump(player, Direction.getRoughDirection(xMod, yMod), 0.1f) { rebuildFov() }
        } else if (newX >= 0 && newY >= 0 && newX < bigWidth && newY < bigHeight && rawDungeon[newX][newY] != '#') {
            layout.slide(player, newX, newY, 0.03f) { rebuildFov() }
        } else {
            layout.bump(player, Direction.getRoughDirection(xMod, yMod), 0.1f)
        }

        phase = Phase.PLAYER_ANIM
    }

    fun handleMoves(hasNext: Boolean, nextCb: () -> Unit) {
        if (layout.isAnimating()) {
            return
        }

        if (pendingMoves.isEmpty()) {
            if (hasNext && phase == Phase.WAIT) {
                nextCb()
            } else {
                when (phase) {
                    Phase.MONSTER_ANIM -> phase = Phase.WAIT
                    Phase.PLAYER_ANIM -> moveMonsters()
                    Dungeon.Phase.WAIT -> {}
                }
            }
            return
        }

        when (phase) {
            Phase.PLAYER_ANIM -> moveMonsters()
            Phase.WAIT, Phase.MONSTER_ANIM -> {
                val move = pendingMoves.removeAt(0)

                if (!mouseInfo.toCursor.isEmpty()) {
                    mouseInfo.toCursor.removeAt(0)
                }

                moveCharacter(move.x - player.gridX, move.y - player.gridY)
                if (pendingMoves.isEmpty()) {
                    mouseInfo.resetPlayerToCursor(player.getCoord(), blockage, true)
                }
            }
        }
    }

    private fun moveMonsters() {
        phase = Phase.MONSTER_ANIM
        val monsterCoords = monsters.keysAsOrderedSet()
        val playerCoord = player.getCoord()

        monsters.forEach { coord, monster ->
            if (visibleTiles[coord.x][coord.y] == 0.0) {
                return@forEach
            }

            getToPlayer.clearGoals()
            val nextMoves = getToPlayer.findPath(1, monsterCoords, null, coord, playerCoord)
            if (nextMoves == null || nextMoves.isEmpty()) {
                return@forEach
            }

            monsterCoords.remove(coord)
            val tmp = nextMoves[0]
            if (tmp.x == playerCoord.x && tmp.y == playerCoord.y) {
                layout.bump(monster, Direction.getRoughDirection(tmp.x - coord.x, tmp.y - coord.y), 0.1f)
                monsterCoords.add(coord)
            } else {
                monsters.alter(coord, tmp)
                layout.slide(monster, tmp.x, tmp.y, 0.03f)
                monsterCoords.add(tmp)
            }
        }
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
