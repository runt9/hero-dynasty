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
import com.runt9.heroDynasty.dungeon.assets.AssetMapper
import com.runt9.heroDynasty.dungeon.hud.HudLayout
import com.runt9.heroDynasty.util.AppConst.baseFov
import com.runt9.heroDynasty.util.AppConst.dungeonHeight
import com.runt9.heroDynasty.util.AppConst.dungeonWidth
import com.runt9.heroDynasty.util.AppConst.viewportHeight
import com.runt9.heroDynasty.util.AppConst.viewportWidth
import com.runt9.heroDynasty.util.toScale
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

    val hudStage: Stage
    private val hud: HudLayout

    internal val rawDungeon: Array<CharArray>

    internal val player: Player = testPlayer
    internal val playerSprite: CharacterSprite
    internal val enemies: OrderedMap<Coord, CharacterSprite> = OrderedMap(20)
    private val fov: FOV

    private val pendingMoves = ArrayList<Coord>(200)

    private var visibleTiles = Array(dungeonWidth.toInt(), { DoubleArray(dungeonHeight.toInt()) })
    private lateinit var blockage: GreasedRegion
    private lateinit var seen: GreasedRegion
    lateinit var resetCursorPath: (Coord, GreasedRegion, Boolean) -> Unit

    private val getToPlayer: DijkstraMap
    val isAnimating get() = layout.isAnimating()

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

        hudStage = Stage(StretchViewport(viewportWidth, viewportHeight), SpriteBatch())
        hud = HudLayout(this)
        hudStage.addActor(hud)

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
        hud.minimap.update(visibleTiles, seen)
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
                layout.bump(playerSprite, Direction.getRoughDirection(xMod, yMod)) { rebuildFov() }
            }
            enemies.containsKey(Coord.get(newX, newY)) -> { // Hit enemy
                val enemy = enemies[Coord.get(newX, newY)]!!
                layout.bump(playerSprite, Direction.getRoughDirection(xMod, yMod)) { rebuildFov() }
                val damage = basicAttack(player, enemy.character)
                if (damage > 0.0) {
                    layout.doFloatingNumber(damage.roundToInt(), playerSprite, enemy)
                    // TODO: Debug roll info?
                    // TODO: Crit info
                    hud.combatLog.add("--> [GREEN]${player.name}[] hits [RED]${enemy.character.name}[] for [RED]${damage.toScale(2)}[] damage!")
                } else {
                    hud.combatLog.add("--> [GREEN]${player.name}[] misses [RED]${enemy.character.name}[].")
                }
            }
            newX >= 0 && newY >= 0 && newX < dungeonWidth && newY < dungeonHeight && rawDungeon[newX][newY] != '#' -> layout.slide(playerSprite, newX, newY) { rebuildFov() } // Move
            else -> layout.bump(playerSprite, Direction.getRoughDirection(xMod, yMod))
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
                        hud.combatLog.newTurn()
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
        // TODO: ArrayOutOfBoundsException
        enemies.forEach { coord, sprite ->
            if (coord != null && !sprite.character.isAlive) {
                sprite.character as Npc
                enemies.remove(coord)
                layout.removeCharacter(sprite)

                // TODO: Level above/below gives more/less xp
                val xp = (sprite.character.level * 100 * sprite.character.powerLevel.xp * rng.between(0.9, 1.2)).toInt() // Bit of randomness for XP
                val gold = player.gainGold(sprite.character.powerLevel.gold)
                val levelledUp = player.gainExperience(xp)

                hud.combatLog.add("[GREEN]${player.name}[] killed [RED]${sprite.character.name}[] and gained [CYAN]$xp[] xp and [GOLD]${gold.toScale(2)}[] gold!")

                if (levelledUp) {
                    layout.doFloatingText("Level Up!", playerSprite)
                    hud.combatLog.add("[GREEN]${player.name}[] is now level [CYAN]${player.level}[]!")
                }

                hud.hoverInfo.update(null)
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
                layout.bump(enemy, Direction.getRoughDirection(tmp.x - coord.x, tmp.y - coord.y))

                // TODO: Sync with player attacks
                val damage = basicAttack(enemy.character, player)
                if (damage > 0.0) {
                    layout.doFloatingNumber(damage.roundToInt(), enemy, playerSprite)
                    hud.combatLog.add("<-- [RED]${enemy.character.name}[] hits [GREEN]${player.name}[] for [RED]${damage.toScale(2)}[] damage!")
                } else {
                    hud.combatLog.add("<-- [RED]${enemy.character.name}[] misses [GREEN]${player.name}.")
                }

                enemyCoords.add(coord)
            } else {
                enemies.alter(coord, tmp)
                layout.slide(enemy, tmp.x, tmp.y)
                enemyCoords.add(tmp)
            }
        }
    }

    fun draw() {
        if (!layout.isbumping) {
            dungeonStage.camera.position.x = playerSprite.x
            dungeonStage.camera.position.y = playerSprite.y
        }

        dungeonStage.act()
        dungeonStage.viewport.apply(false)
        dungeonStage.draw()

        hudStage.act()
        hudStage.draw()
    }

    fun showInfoForCursor(coord: Coord) {
        val character = when {
            playerSprite.coord == coord -> player
            enemies.containsKey(coord) -> enemies[coord]!!.character
            else -> null
        }

        hud.hoverInfo.update(character)
    }

    fun hasPendingMoves() = !pendingMoves.isEmpty()
    fun moveTo(path: List<Coord>) = pendingMoves.addAll(path)
    fun isVisible(x: Int, y: Int) = visibleTiles[x][y] > 0.0
    fun isVisible(coord: Coord) = isVisible(coord.x, coord.y)
    private fun enemiesVisible() = enemies.any { isVisible(it.key) }
}
