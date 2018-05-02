package com.runt9.heroDynasty.dungeon.hud

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.scenes.scene2d.Group
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.runt9.heroDynasty.dungeon.Dungeon
import com.runt9.heroDynasty.dungeon.hud.widgets.*
import com.runt9.heroDynasty.util.AppConst.viewportHeight
import com.runt9.heroDynasty.util.AppConst.viewportWidth


// TODO: Let's make some constants
class HudLayout(dungeon: Dungeon) : Group() {
    private val player = dungeon.player
    private val skin = Skin(Gdx.files.internal("uiskin.json"))

    private val hotBar = HotBar()
    private val healthBar = HealthBar(player)
    private val infoPanel = InfoPanel(skin, player)
    internal val minimap = Minimap(dungeon.rawDungeon, dungeon.playerSprite, dungeon.enemies)
    internal val combatLog = CombatLog(skin)
    internal val hoverInfo = HoverInfo()

    init {
        skin.get("default-font", BitmapFont::class.java).data.markupEnabled = true

        addActor(hotBar)
        addActor(healthBar)
        addActor(infoPanel)
        addActor(minimap)
        addActor(combatLog)
        addActor(hoverInfo)

        infoPanel.x = 128f
        infoPanel.update()

        minimap.x = viewportWidth - 256f
        minimap.width = 256f
        minimap.height = 256f

        combatLog.x = 256f
        combatLog.y = viewportHeight - 128f

        hotBar.x = 64f * 6
        hotBar.y = 36f // TODO: Magic?

        healthBar.x = 64f * 6
        healthBar.y = 72f

        hoverInfo.x = viewportWidth - 256f
        hoverInfo.y = viewportHeight - 512f
    }
}