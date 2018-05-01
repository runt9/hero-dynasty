package com.runt9.heroDynasty.dungeon.assets

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator

// TODO: Handle different fonts
fun getFont(pt: Int): BitmapFont {
    val fontGenerator = FreeTypeFontGenerator(Gdx.files.internal("DroidSans.ttf"))
    val parameter = FreeTypeFontGenerator.FreeTypeFontParameter()
    parameter.size = pt
    val font = fontGenerator.generateFont(parameter)
    font.data.markupEnabled = true
    return font
}