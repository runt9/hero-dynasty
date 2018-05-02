package com.runt9.heroDynasty.dungeon

import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.utils.viewport.StretchViewport
import com.runt9.heroDynasty.character.ModifierType
import com.runt9.heroDynasty.character.Player
import com.runt9.heroDynasty.character.npc.Npc
import com.runt9.heroDynasty.character.testData.testEnemies
import com.runt9.heroDynasty.character.testData.testPlayer
import com.runt9.heroDynasty.core.basicAttack
import com.runt9.heroDynasty.core.randomChance
import com.runt9.heroDynasty.core.randomItem
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
import com.runt9.heroDynasty.util.toAssetName
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

// TODO: Needs some refactoring
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
        layout = DungeonLayout(rawDungeon, assetMapper.getAssetMap(), assetMapper)
        val floors = GreasedRegion(rawDungeon, '.')
        val playerCoord = floors.singleRandom(rng)
        playerSprite = layout.addCharacter(assetMapper.getRegion(player.race::class.simpleName!!.toAssetName()), playerCoord, player)
        floors.remove(element = playerCoord)
        testEnemies.forEach {
            val enemyCoord = floors.singleRandom(rng)
            floors.remove(element = enemyCoord)
            enemies[enemyCoord] = layout.addCharacter(assetMapper.getRegion(it.name.toAssetName()), enemyCoord, it)
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
            xMod == 0 && yMod == 0 -> {
            } // no-op to skip turn
            rawDungeon[newX][newY] == '+' -> { // Open Door
                layout.openDoor(newX, newY)
                layout.bump(playerSprite, Direction.getRoughDirection(xMod, yMod)) { rebuildFov() }
            }
            enemies.containsKey(Coord.get(newX, newY)) -> attack(playerSprite, enemies[Coord.get(newX, newY)]!!)
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
                    Dungeon.Phase.WAIT -> {
                    }
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
                dropItems(sprite.character)

                hud.combatLog.add("[GREEN]${player.name}[] killed [RED]${sprite.character.name}[] and gained [CYAN]$xp[] xp and [GOLD]${gold.toScale(2)}[] gold!")

                if (levelledUp) {
                    layout.doFloatingText("Level Up!", playerSprite)
                    hud.combatLog.add("[GREEN]${player.name}[] is now level [CYAN]${player.level}[]!")
                }

                if (hud.hoverInfo.isCharacter(sprite.character)) {
                    hud.hoverInfo.update(null)
                }
            }
        }
    }

    // TODO: Chest drops
    private fun dropItems(enemy: Npc, baseChance: Int = 50) {
        var chance = baseChance * enemy.powerLevel.itemDrop
        for (i in 0..3) { // Max 3 items
            if (!randomChance(baseChance = chance, multiplier = player.getModifier(ModifierType.DROP_CHANCE)).success) {
                break
            }

            val item = randomItem()
            item.rollStats(player.getModifier(ModifierType.RARITY_FIND), chance)
            player.itemVault.add(item)

            val color = item.rarity.color
            hud.combatLog.add("[GREEN]${player.name}[] found [#$color]${item.getName()}[]!")
            chance /= 2
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
                attack(enemy, playerSprite)
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

    fun moveTo(path: List<Coord>) {
        // TODO: Proper line of sight!
        if (enemies.containsKey(path.last()) && player.inventory.primaryHand != null && player.inventory.primaryHand!!.range >= path.size) {
            pendingMoves.add(path.last())
        } else {
            pendingMoves.addAll(path)
        }
    }

    private fun attack(attacker: CharacterSprite, defender: CharacterSprite) {
        layout.bump(attacker, Direction.getRoughDirection(defender.gridX - attacker.gridX, defender.gridY - attacker.gridY))
        val damage = basicAttack(attacker.character, defender.character)
        if (damage > 0.0) {
            layout.doFloatingNumber(damage.roundToInt(), attacker, defender)
            // TODO: Debug roll info?
            // TODO: Crit info
            hud.combatLog.add("${attacker.character.name} [RED]hits[] ${defender.character.name} for [RED]${damage.toScale(2)}[] damage!")
        } else {
            hud.combatLog.add("${attacker.character.name} [GRAY]misses[] ${defender.character.name}.")
        }
    }

    fun hasPendingMoves() = pendingMoves.isNotEmpty()
    fun isVisible(x: Int, y: Int) = visibleTiles[x][y] > 0.0
    fun isVisible(coord: Coord) = isVisible(coord.x, coord.y)
    private fun enemiesVisible() = enemies.any { isVisible(it.key) }
}
