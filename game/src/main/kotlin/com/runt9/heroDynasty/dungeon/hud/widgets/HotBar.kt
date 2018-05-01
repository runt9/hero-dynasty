package com.runt9.heroDynasty.dungeon.hud.widgets

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.Pixmap
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.scenes.scene2d.ui.HorizontalGroup
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.badlogic.gdx.scenes.scene2d.ui.WidgetGroup

class HotBar : WidgetGroup() {
    private val hotBar = HorizontalGroup()
    init {
        (0 until 12).forEach {
            val pixmap = Pixmap(64, 64, Pixmap.Format.RGBA8888)
            pixmap.setColor(Color.WHITE)
            pixmap.drawRectangle(0, 0, 64, 64)
            val image = Image(Texture(pixmap))
            image.x = 64f * it
            hotBar.addActor(image)
        }

        addActor(hotBar)
    }
}