package com.runt9.heroDynasty.lib

import squidpony.squidgrid.gui.gdx.SColor

object AppConst {
    const val gridWidth = 50
    const val gridHeight = 25
    const val bigWidth = gridWidth * 2
    const val bigHeight = gridHeight * 2
    const val cellWidth = 32
    const val cellHeight = 32
    const val viewportWidth: Float = (gridWidth * cellWidth).toFloat()
    const val viewportHeight: Float = (gridHeight * cellHeight).toFloat()
    const val baseFov = 9.0

    val bgColor: SColor = SColor.DARK_SLATE_GRAY
}