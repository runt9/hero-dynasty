package com.runt9.heroDynasty.dungeon.hud.widgets

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.scenes.scene2d.ui.*
import com.runt9.heroDynasty.core.getPixmapDrawable
import com.runt9.heroDynasty.dungeon.assets.getFont


// TODO: Find out if we need a maximum number of rows. Probably do because of memory
class CombatLog(skin: Skin) : WidgetGroup() {
    private val container = Table()
    private val combatLog = Table()
    private var combatLogAdded = false
    private val combatLogScroll: ScrollPane

    init {
        container.setFillParent(true)

        val scrollPaneStyle = skin.get(ScrollPane.ScrollPaneStyle::class.java)
        scrollPaneStyle.background = getPixmapDrawable(512, 256, Color(0.5f, 0.5f, 0.5f, 0.1f))
        combatLogScroll = ScrollPane(combatLog, scrollPaneStyle)
        combatLogScroll.setSmoothScrolling(false)

        combatLog.pad(10f).defaults().space(4f).width(475f)
        container.add(combatLogScroll).size(512f, 256f)
        addActor(container)
    }

    fun add(message: String) {
        combatLog.row()
        val label = Label(message, Label.LabelStyle(getFont(12), Color.WHITE))
        label.setWrap(true)
        combatLog.add(label)
        combatLogAdded = true
        combatLogScroll.scrollTo(0f, 0f, 0f, 0f)
    }

    fun newTurn() {
        if (combatLogAdded) {
            add("----------------------------------------------------------")
        }

        combatLogAdded = false
    }
}