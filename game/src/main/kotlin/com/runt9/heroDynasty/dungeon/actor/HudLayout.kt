package com.runt9.heroDynasty.dungeon.actor

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.Pixmap
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.GlyphLayout
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.Group
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.ui.*
import com.runt9.heroDynasty.character.Player
import com.runt9.heroDynasty.core.getPixmapDrawable
import com.runt9.heroDynasty.dungeon.Dungeon
import com.runt9.heroDynasty.util.AppConst.viewportWidth
import com.runt9.heroDynasty.util.toScale
import squidpony.squidmath.Coord
import squidpony.squidmath.GreasedRegion
import squidpony.squidmath.OrderedMap

// TODO: Let's make some constants
// TODO: Real refactor. Like lots of it
class HudLayout(private val dungeon: Dungeon) : Group() {
    private val player = dungeon.player
    private val skin = Skin(Gdx.files.internal("uiskin.json"))
    private val hotBar = buildHotbar()
    private val healthBar = buildHealthBar()
    private val infoPanel = buildInfoPanel()
    private val shortcutButtons = buildShortcutButtons()
    private val minimap = buildMinimap()

    init {
        addActor(hotBar)
        addActor(healthBar)
        addActor(infoPanel)
//        addActor(shortcutButtons)
        addActor(minimap)
        updateInfoPanel()

        val healthBarText = HealthBarText(player)
        healthBarText.y = 86f
        addActor(healthBarText)
    }

    override fun draw(batch: Batch, parentAlpha: Float) {
        healthBar.value = player.hitPoints.current.toFloat()
        updateInfoPanel()
        super.draw(batch, parentAlpha)
    }

    private fun buildHotbar(): HorizontalGroup {
        val hotbar = HorizontalGroup()
        (0 until 12).forEach {
            val pixmap = Pixmap(64, 64, Pixmap.Format.RGBA8888)
            pixmap.setColor(Color.WHITE)
            pixmap.drawRectangle(0, 0, 64, 64)
            val image = Image(Texture(pixmap))
            image.x = (64f * 6) + 64f * it
            hotbar.addActor(image)
        }

        hotbar.x = 64f * 6
        hotbar.y = 36f // TODO: Magic?
        return hotbar
    }

    private fun buildHealthBar(): ProgressBar {
        val progressBarStyle = ProgressBar.ProgressBarStyle()
        progressBarStyle.background = getPixmapDrawable(12 * 64, 16, Color.RED)
        progressBarStyle.knob = getPixmapDrawable(0, 16, Color.GREEN)
        progressBarStyle.knobBefore = getPixmapDrawable(12 * 64, 16, Color.GREEN)

        val healthBar = ProgressBar(0.0f, player.hitPoints.max.toFloat(), 0.01f, false, progressBarStyle)
        healthBar.value = player.hitPoints.current.toFloat()
        healthBar.setBounds(64f * 6, 72f, 12 * 64f, 16f)

        return healthBar
    }

    private fun buildInfoPanel(): Table {
        val table = Table()
        table.x = 128f
        table.y = 64f
        return table
    }

    private fun updateInfoPanel() {
        infoPanel.reset()
        infoPanel.add(Label("Player Info", skin)).colspan(2)
        infoPanel.row()
        infoPanel.add(Label("Level:", skin))
        infoPanel.add(Label(player.level.toString(), skin))
        infoPanel.row()
        infoPanel.add(Label("XP:", skin))
        infoPanel.add(Label("${player.xp} / ${player.xpToNextLevel}", skin))
        infoPanel.row()
        infoPanel.add(Label("Gold:", skin))
        infoPanel.add(Label(player.inventory.gold.toScale(2).toString(), skin))
    }

    private fun buildShortcutButtons(): Table {
        val table = Table()
        table.x = viewportWidth - 256f
        table.y = 40f

        // TODO: Clicks increase the size of the button (stupidly) which messes with the table.
        val inventoryButton = TextButton("Inventory", skin)
        inventoryButton.addListener {
            if (it is InputEvent && it.type == InputEvent.Type.touchUp) {
                System.out.println("Inventory Clicked")
            }

            return@addListener true
        }

        val characterButton = TextButton("Character", skin)
        characterButton.addListener {
            if (it is InputEvent && it.type == InputEvent.Type.touchUp) {
                System.out.println("Character Clicked")
            }

            return@addListener true
        }

        val settingsButton = TextButton("Settings", skin)
        settingsButton.addListener {
            if (it is InputEvent && it.type == InputEvent.Type.touchUp) {
                System.out.println("Settings Clicked")
            }

            return@addListener true
        }

        val menuButton = TextButton("Menu", skin)
        menuButton.addListener {
            if (it is InputEvent && it.type == InputEvent.Type.touchUp) {
                System.out.println("Menu Clicked")
            }

            return@addListener true
        }


        table.defaults().width(128f).uniform()
        table.add(inventoryButton)
        table.add(characterButton)
        table.row()
        table.add(settingsButton)
        table.add(menuButton)
        return table
    }

    private fun buildMinimap(): Minimap {
        val minimap = Minimap(dungeon.rawDungeon, dungeon.playerSprite, dungeon.enemies)
        minimap.x = viewportWidth - 256f
        minimap.y = 0f
        minimap.width = 256f
        minimap.height = 256f
        return minimap
    }

    fun updateMinimap(visible: Array<DoubleArray>, seen: GreasedRegion) {
        minimap.visible = visible
        minimap.seen = seen
    }

    class HealthBarText(private val player: Player) : Actor() {
        private val font = BitmapFont()
        private val glyphLayout = GlyphLayout()

        override fun draw(batch: Batch, parentAlpha: Float) {
            font.color = Color.WHITE
            val text = "${player.hitPoints.current.toInt()} / ${player.hitPoints.max.toInt()} (${player.hitPoints.regen.toScale(2)})"
            glyphLayout.setText(font, text)
            x = (64f * 12) - (glyphLayout.width / 2)
            font.draw(batch, glyphLayout, x, y)
        }
    }

    class Minimap(private val dungeon: Array<CharArray>, private val player: CharacterSprite, private val enemies: OrderedMap<Coord, CharacterSprite>) : Actor() {
        private val font = BitmapFont()
        internal lateinit var seen: GreasedRegion
        internal lateinit var visible: Array<DoubleArray>

        override fun draw(batch: Batch, parentAlpha: Float) {
            val bgPixmap = Pixmap(256, 256, Pixmap.Format.RGBA8888)
            bgPixmap.setColor(Color.LIGHT_GRAY)
            bgPixmap.fill()
            batch.draw(Texture(bgPixmap), x, y)

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

            font.data.scaleX = 1.5f
            font.data.scaleY = 1.5f

            font.color = Color.GREEN
            // TODO: Magic 5 to account for font size
            font.draw(batch, ".", x + player.coord.x * 3f, y + (player.coord.y + 5) * 3f)

            font.color = Color.RED
            enemies.forEach { coord, _ ->
                if (visible[coord.x][coord.y] > 0.0) {
                    font.draw(batch, ".", x + coord.x * 3f, y + (coord.y + 5) * 3f)
                }
            }
        }
    }
}