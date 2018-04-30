package com.runt9.heroDynasty.dungeon

import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.utils.viewport.StretchViewport
import com.runt9.heroDynasty.character.Npc
import com.runt9.heroDynasty.character.Player
import com.runt9.heroDynasty.character.testData.testEnemies
import com.runt9.heroDynasty.character.testData.testPlayer
import com.runt9.heroDynasty.core.basicAttack
import com.runt9.heroDynasty.core.rng
import com.runt9.heroDynasty.dungeon.actor.CharacterSprite
import com.runt9.heroDynasty.dungeon.actor.DungeonLayout
import com.runt9.heroDynasty.dungeon.actor.HudLayout
import com.runt9.heroDynasty.dungeon.assets.AssetMapper
import com.runt9.heroDynasty.util.AppConst.baseFov
import com.runt9.heroDynasty.util.AppConst.bigHeight
import com.runt9.heroDynasty.util.AppConst.bigWidth
import com.runt9.heroDynasty.util.AppConst.viewportHeight
import com.runt9.heroDynasty.util.AppConst.viewportWidth
import squidpony.squidai.DijkstraMap
import squidpony.squidgrid.Direction
import squidpony.squidgrid.FOV
import squidpony.squidgrid.Radius
import squidpony.squidgrid.mapping.DungeonUtility
import squidpony.squidmath.Coord
import squidpony.squidmath.GreasedRegion
import squidpony.squidmath.OrderedMap
import kotlin.math.roundToInt

class Dungeon {
    enum class Phase { WAIT, PLAYER_ANIM, ENEMY_ANIM }
    var phase = Phase.WAIT

    val dungeonStage: Stage
    private val layout: DungeonLayout

    val hud: Stage
    private val hudLayout: HudLayout

    internal val rawDungeon: Array<CharArray>

    internal val player: Player = testPlayer
    internal val playerSprite: CharacterSprite
    internal val enemies: OrderedMap<Coord, CharacterSprite> = OrderedMap(20)
    private val fov: FOV

    private val pendingMoves = ArrayList<Coord>(200)

    private var visibleTiles = Array(bigWidth.toInt(), { DoubleArray(bigHeight.toInt()) })
    private lateinit var blockage: GreasedRegion
    internal lateinit var seen: GreasedRegion
    lateinit var resetCursorPath: (Coord, GreasedRegion, Boolean) -> Unit

    private val getToPlayer: DijkstraMap

    init {
        val assetMapper = AssetMapper()

        rawDungeon = DungeonGenerator().generateDungeon()
        layout = DungeonLayout(rawDungeon, assetMapper.getAssetMap())
        val floors = GreasedRegion(rawDungeon, '.')
        val playerCoord = floors.singleRandom(rng)
        playerSprite = layout.addCharacter(assetMapper.getCharacter(), playerCoord, player)
        floors.remove(element = playerCoord)
        testEnemies.forEach {
            val enemyCoord = floors.singleRandom(rng)
            floors.remove(element = enemyCoord)
            enemies[enemyCoord] = layout.addCharacter(assetMapper.getEnemy(), enemyCoord, it)
        }
        getToPlayer = DijkstraMap(rawDungeon, DijkstraMap.Measurement.EUCLIDEAN)

        dungeonStage = Stage(StretchViewport(viewportWidth, viewportHeight), SpriteBatch())
        dungeonStage.addActor(layout)

        hud = Stage(StretchViewport(viewportWidth, viewportHeight), SpriteBatch())
        hudLayout = HudLayout(this)
        hud.addActor(hudLayout)

        fov = FOV(FOV.RIPPLE)
    }

    fun rebuildFov() {
        visibleTiles = fov.calculateFOV(DungeonUtility.generateResistances(rawDungeon), playerSprite.gridX, playerSprite.gridY, baseFov, Radius.CIRCLE)
        if (!::blockage.isInitialized) {
            blockage = GreasedRegion(visibleTiles, 0.0)
            seen = blockage.not().copy()
        } else {
            blockage.refill(visibleTiles, 0.0)
            seen.or(blockage.not())
        }

        blockage.fringe8way()
        resetCursorPath(playerSprite.coord, blockage, false)
        layout.updateVision(visibleTiles, seen)
        hudLayout.updateMinimap(visibleTiles, seen)
    }

    fun queueNewMove(coord: Coord) = pendingMoves.add(Coord.get(playerSprite.gridX + coord.x, playerSprite.gridY + coord.y))

    private fun moveCharacter(xMod: Int, yMod: Int) {
        val newX = playerSprite.gridX + xMod
        val newY = playerSprite.gridY + yMod

        // Probably a better place for this stuff
        when {
            xMod == 0 && yMod == 0 -> {} // no-op to skip turn
            rawDungeon[newX][newY] == '+' -> { // Open Door
                layout.openDoor(newX, newY)
                layout.bump(playerSprite, Direction.getRoughDirection(xMod, yMod), 0f) { rebuildFov() }
            }
            enemies.containsKey(Coord.get(newX, newY)) -> { // Hit enemy
                layout.bump(playerSprite, Direction.getRoughDirection(xMod, yMod), 0.1f) { rebuildFov() }
                val damage = basicAttack(player, enemies[Coord.get(newX, newY)]!!.character)
                if (damage > 0.0) {
                    layout.doFloatingNumber(damage.roundToInt(), playerSprite, enemies[Coord.get(newX, newY)]!!)
                }
            }
            newX >= 0 && newY >= 0 && newX < bigWidth && newY < bigHeight && rawDungeon[newX][newY] != '#' -> layout.slide(playerSprite, newX, newY, 0.03f) { rebuildFov() } // Move
            else -> layout.bump(playerSprite, Direction.getRoughDirection(xMod, yMod), 0.1f)
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
                    // New turn?
                    Phase.ENEMY_ANIM -> {
                        phase = Phase.WAIT
                        player.hitPoints.doRegen()
                        // TODO: Enemies regen too?
                    }
                    Phase.PLAYER_ANIM -> moveEnemies()
                    Dungeon.Phase.WAIT -> {}
                }
            }
            return
        }

        when (phase) {
            Phase.PLAYER_ANIM -> moveEnemies()
            Phase.WAIT, Phase.ENEMY_ANIM -> {
                val move = pendingMoves.removeAt(0)

                moveCharacter(move.x - playerSprite.gridX, move.y - playerSprite.gridY)

                // If we moved and now see an enemy, clear out our moves so we can react and now only can move one space
                if (enemiesVisible()) {
                    pendingMoves.clear()
                }

                if (pendingMoves.isEmpty()) {
                    resetCursorPath(playerSprite.coord, blockage, true)
                }
            }
        }

        // TODO: Better place to kill since drops and stuff occur
        enemies.forEach { coord, sprite ->
            if (coord != null && !sprite.character.isAlive) {
                sprite.character as Npc
                enemies.remove(coord)
                layout.removeCharacter(sprite)
                // TODO: Level above/below gives more/less xp
                val levelledUp = player.gainExperience((sprite.character.level * 100 * sprite.character.powerLevel.xp).toInt())
                if (levelledUp) {
                    layout.doFloatingText("Level Up!", playerSprite)
                }
                player.gainGold(sprite.character.powerLevel.gold)
            }
        }
    }

    // TODO: Refactor
    private fun moveEnemies() {
        phase = Phase.ENEMY_ANIM
        val enemyCoords = enemies.keysAsOrderedSet()
        val playerCoord = playerSprite.coord

        enemies.forEach { coord, enemy ->
            // TODO: I have NO IDEA why coord can be null, probably something to do with this guy's OrderedMap implementation
            if (coord == null || visibleTiles[coord.x][coord.y] == 0.0) {
                return@forEach
            }

            getToPlayer.clearGoals()
            // TODO: Update pathfinding to still be willing to follow enemies to player
            val nextMoves = getToPlayer.findPath(1, enemyCoords, null, coord, playerCoord)
            if (nextMoves == null || nextMoves.isEmpty()) {
                return@forEach
            }

            enemyCoords.remove(coord)
            val tmp = nextMoves[0]
            if (tmp.x == playerCoord.x && tmp.y == playerCoord.y) {
                layout.bump(enemy, Direction.getRoughDirection(tmp.x - coord.x, tmp.y - coord.y), 0.1f)

                val damage = basicAttack(enemy.character, player)
                if (damage > 0.0) {
                    layout.doFloatingNumber(damage.roundToInt(), enemy, playerSprite)
                }

                enemyCoords.add(coord)
            } else {
                enemies.alter(coord, tmp)
                layout.slide(enemy, tmp.x, tmp.y, 0.03f)
                enemyCoords.add(tmp)
            }
        }
    }

    fun draw() {
        dungeonStage.camera.position.x = playerSprite.x
        dungeonStage.camera.position.y = playerSprite.y
        dungeonStage.act()
        dungeonStage.viewport.apply(false)
        dungeonStage.draw()

        hud.act()
        hud.draw()
    }

    fun hasPendingMoves() = !pendingMoves.isEmpty()
    fun moveTo(path: List<Coord>) = pendingMoves.addAll(path)
    fun isVisible(x: Int, y: Int) = visibleTiles[x][y] > 0.0
    fun isVisible(coord: Coord) = isVisible(coord.x, coord.y)
    private fun enemiesVisible() = enemies.any { isVisible(it.key) }
}
