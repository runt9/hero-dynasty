package com.runt9.heroDynasty.core

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.Pixmap
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable

fun getPixmapDrawable(width: Int, height: Int, color: Color): TextureRegionDrawable {
    val pixmap = Pixmap(width, height, Pixmap.Format.RGBA8888)
    pixmap.setColor(color)
    pixmap.fill()
    return TextureRegionDrawable(TextureRegion(Texture(pixmap)))
}